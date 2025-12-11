#!/usr/bin/env python3
import random
import datetime
import mysql.connector

CONFIG = {
    "host": "127.0.0.1",
    "port": 3307,
    "user": "powerup_user",
    "password": "powerup_pass",
    "database": "powerup",
}

random.seed(42)


def connect():
    return mysql.connector.connect(**CONFIG)


def assert_required_tables(cur):
    required = {
        "usuario",
        "perfil",
        "avatar",
        "perfil_amigos",
        "equipe",
        "equipe_membros",
        "feedback",
        "conquista",
        "perfil_conquista",
        "frequencia",
    }
    cur.execute(
        """
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = %s
        """,
        (CONFIG["database"],),
    )
    existing = {row[0] for row in cur.fetchall()}
    missing = required - existing
    if missing:
        raise RuntimeError(
            f"Tabelas obrigatorias ausentes no schema '{CONFIG['database']}': {sorted(missing)}. "
            "Execute as migrations/criacao de schema antes de rodar o seed."
        )


def get_table_columns(cur, table):
    cur.execute(
        """
        SELECT column_name
        FROM information_schema.columns
        WHERE table_schema = %s AND table_name = %s
        """,
        (CONFIG["database"], table),
    )
    return {row[0] for row in cur.fetchall()}


def get_columns_details(cur, table):
    cur.execute(
        """
        SELECT column_name, is_nullable, column_default
        FROM information_schema.columns
        WHERE table_schema = %s AND table_name = %s
        """,
        (CONFIG["database"], table),
    )
    return {
        row[0]: {"nullable": row[1] == "YES", "has_default": row[2] is not None}
        for row in cur.fetchall()
    }


def exec_many(cur, sql, rows):
    if rows:
        cur.executemany(sql, rows)


def truncate_all(cur):
    cur.execute("SET FOREIGN_KEY_CHECKS=0;")
    for tbl in [
        "avatar",
        "perfil_amigos",
        "equipe_membros",
        "equipe",
        "feedback",
        "frequencia",
        "plano_refeicoes",
        "refeicao_alimentos",
        "refeicao",
        "plano_nutricional",
        "plano_treino_exercicios",
        "plano_treino",
        "conquista_criterio",
        "perfil_conquista",
        "conquista",
        "perfil_meta",
        "meta",
        "perfil",
        "usuario",
    ]:
        try:
            cur.execute(f"TRUNCATE TABLE {tbl};")
        except mysql.connector.Error as e:
            # Ignora tabelas que possam nao existir no schema atual
            if e.errno == 1146:  # Table doesn't exist
                continue
            raise
    cur.execute("SET FOREIGN_KEY_CHECKS=1;")


def seed_usuarios_perfis_avatares(cur, n=30):
    usuarios, perfis, avatares = [], [], []
    base_date = datetime.date(1990, 1, 1)
    perfis_info = []
    for i in range(1, n + 1):
        email = f"user{i}@example.com"
        nome = f"User {i}"
        senha = "senha123"
        dnasc = base_date + datetime.timedelta(days=300 * i)
        usuarios.append((email, nome, senha, dnasc))
    exec_many(cur, "INSERT INTO usuario (email, nome, senha, data_nascimento) VALUES (%s,%s,%s,%s)", usuarios)

    cur.execute("SELECT id, email FROM usuario")
    rows = cur.fetchall()
    for uid, email in rows:
        username = email.split("@")[0]
        foto = None
        perfis.append((uid, email, username, True, datetime.datetime.now(), foto))
        perfis_info.append({"perfil_id": uid, "email": email})
    exec_many(cur, "INSERT INTO perfil (id, usuario_email, username, estado, criacao, foto) VALUES (%s,%s,%s,%s,%s,%s)", perfis)

    for pid, email in rows:
        nivel = random.randint(3, 18)
        experiencia = random.randint(0, 99)
        dinheiro = random.randint(500, 2000)
        forca = random.randint(5, 20)
        avatares.append((pid, nivel, experiencia, dinheiro, forca))
    exec_many(cur, "INSERT INTO avatar (perfil_id, nivel, experiencia, dinheiro, forca) VALUES (%s,%s,%s,%s,%s)", avatares)
    return perfis_info


def seed_amizades(cur, max_links=40):
    cur.execute("SELECT id FROM perfil")
    perfil_ids = [r[0] for r in cur.fetchall()]
    links = set()
    while len(links) < max_links and len(links) < len(perfil_ids) * 2:
        a, b = random.sample(perfil_ids, 2)
        if a == b:
            continue
        key = tuple(sorted((a, b)))
        if key in links:
            continue
        links.add(key)
    rows = []
    for a, b in links:
        rows.append((a, b))
        rows.append((b, a))
    exec_many(cur, "INSERT INTO perfil_amigos (perfil_id, amigo_perfil_id) VALUES (%s,%s)", rows)


def seed_equipes(cur, team_count=4):
    cur.execute("SELECT id, usuario_email FROM perfil ORDER BY id")
    perfis = cur.fetchall()
    equipes_rows, membros_rows = [], []
    for t in range(1, team_count + 1):
        nome = f"Equipe {t}"
        desc = f"Time de teste {t}"
        foto = None
        inicio = datetime.date.today() - datetime.timedelta(days=30 * t)
        fim = None
        adm_email = random.choice(perfis)[1]
        equipes_rows.append((nome, desc, foto, inicio, fim, adm_email))
    exec_many(cur, "INSERT INTO equipe (nome, descricao, foto, inicio, fim, usuario_adm) VALUES (%s,%s,%s,%s,%s,%s)", equipes_rows)

    cur.execute("SELECT id FROM equipe")
    equipe_ids = [r[0] for r in cur.fetchall()]
    for eid in equipe_ids:
        membros = random.sample(perfis, k=min(6, len(perfis)))
        for pid, email in membros:
            membros_rows.append((eid, email))
    exec_many(cur, "INSERT INTO equipe_membros (equipe_id, usuario_email) VALUES (%s,%s)", membros_rows)


def seed_feedback(cur, count=60):
    cur.execute("SELECT usuario_email FROM perfil")
    emails = [r[0] for r in cur.fetchall()]
    classifs = ["Cansado", "Bom", "Excelente", "ComDor"]
    rows = []
    cols = get_table_columns(cur, "feedback")
    # Nomes esperados pelo JPA: frequencia_id, classificacao, feedback_texto, email_usuario, data
    usa_freq = "frequencia_id" in cols
    usa_data = "data" in cols
    usa_email = "email_usuario" in cols
    usa_class = "classificacao" in cols
    usa_texto = "feedback_texto" in cols
    if not (usa_email and usa_class and usa_texto):
        print("Aviso: tabela feedback nao possui colunas basicas esperadas; pulando seed de feedback.")
        return
    for _ in range(count):
        email = random.choice(emails)
        freq = random.randint(1, 10)
        texto = f"Feedback {freq} de {email}"
        data = datetime.datetime.now() - datetime.timedelta(days=random.randint(0, 30))
        row = []
        insert_cols = []
        if usa_freq:
            insert_cols.append("frequencia_id")
            row.append(freq)
        insert_cols.append("email_usuario")
        row.append(email)
        insert_cols.append("classificacao")
        row.append(random.choice(classifs))
        insert_cols.append("feedback_texto")
        row.append(texto)
        if usa_data:
            insert_cols.append("data")
            row.append(data)
        rows.append((tuple(insert_cols), tuple(row)))

    # Agrupa por conjunto de colunas iguais para rodar um insert por grupo
    grouped = {}
    for cols_tuple, values in rows:
        grouped.setdefault(cols_tuple, []).append(values)
    for cols_tuple, values_list in grouped.items():
        cols_join = ", ".join(cols_tuple)
        placeholders = ", ".join(["%s"] * len(cols_tuple))
        sql = f"INSERT INTO feedback ({cols_join}) VALUES ({placeholders})"
        exec_many(cur, sql, values_list)


def seed_conquistas(cur, count=12):
    details = get_columns_details(cur, "conquista")
    cols = set(details.keys())

    base_cols = ["nome", "descricao"]
    has_concluida = "concluida" in cols
    has_badge = "badge" in cols

    # Campos obrigatorios adicionais sem default: vamos preencher valores dummy (exercicio_id/treino_id)
    required_extra = [
        c
        for c, meta in details.items()
        if c not in {"id", "nome", "descricao", "concluida", "badge"} and not meta["nullable"] and not meta["has_default"]
    ]

    insert_cols = base_cols.copy()
    if has_concluida:
        insert_cols.append("concluida")
    if has_badge:
        insert_cols.append("badge")
    # Insere obrigatorios extras no final
    for c in required_extra:
        insert_cols.append(c)

    conquistas = []
    for i in range(1, count + 1):
        row = [f"Conquista {i}", f"Descricao {i}"]
        if has_concluida:
            row.append(False)
        if has_badge:
            row.append(None)
        for c in required_extra:
            if c == "exercicio_id":
                row.append(1)
            elif c == "treino_id":
                row.append(1)
            else:
                row.append(None)
        conquistas.append(tuple(row))

    cols_join = ", ".join(insert_cols)
    placeholders = ", ".join(["%s"] * len(insert_cols))
    sql = f"INSERT INTO conquista ({cols_join}) VALUES ({placeholders})"
    exec_many(cur, sql, conquistas)

    cur.execute("SELECT id FROM conquista")
    conquistas_ids = [r[0] for r in cur.fetchall()]
    cur.execute("SELECT id FROM perfil")
    perfis_ids = [r[0] for r in cur.fetchall()]

    perfil_conq = []
    for pid in perfis_ids:
        for cid in random.sample(conquistas_ids, k=min(3, len(conquistas_ids))):
            perfil_conq.append((pid, cid))
    exec_many(cur, "INSERT INTO perfil_conquista (perfil_id, conquista_id) VALUES (%s,%s)", perfil_conq)


def seed_frequencia(cur, count=80):
    cur.execute("SELECT id, usuario_email FROM perfil")
    perfis_rows = cur.fetchall()
    email_by_perfil = {r[0]: r[1] for r in perfis_rows}
    # tentar obter plano_treino_ids e relacionar por email
    cur.execute("SELECT id, usuario_email FROM plano_treino")
    planos = cur.fetchall()
    plano_by_email = {}
    for pid, email in planos:
        plano_by_email.setdefault(email, []).append(pid)

    # obter treinos vinculados via join se existir
    cur.execute("SELECT plano_id, treino_id FROM plano_treino_treinos")
    plano_treinos_map = {}
    for plano_id, treino_id in cur.fetchall():
        plano_treinos_map.setdefault(plano_id, []).append(treino_id)

    details = get_columns_details(cur, "frequencia")
    cols = set(details.keys())

    insert_rows = []
    for _ in range(count):
        pid = random.choice(list(email_by_perfil.keys()))
        data_val = datetime.datetime.now() - datetime.timedelta(days=random.randint(0, 14))
        row = []
        insert_cols = []

        # perfil_id
        if "perfil_id" in cols:
            insert_cols.append("perfil_id")
            row.append(pid)
        # plano_treino_id
        if "plano_treino_id" in cols:
            insert_cols.append("plano_treino_id")
            email = email_by_perfil.get(pid)
            planos_email = plano_by_email.get(email, [])
            row.append(planos_email[0] if planos_email else 1)
        # treino_id
        if "treino_id" in cols:
            insert_cols.append("treino_id")
            escolhido = None
            email = email_by_perfil.get(pid)
            planos_email = plano_by_email.get(email, [])
            if planos_email:
                for pe in planos_email:
                    tlist = plano_treinos_map.get(pe, [])
                    if tlist:
                        escolhido = random.choice(tlist)
                        break
            row.append(escolhido if escolhido is not None else 1)
        # data_presenca ou data
        if "data_presenca" in cols:
            insert_cols.append("data_presenca")
            row.append(data_val)
        elif "data" in cols:
            insert_cols.append("data")
            row.append(data_val)
        # foto
        if "foto" in cols:
            insert_cols.append("foto")
            row.append(None)

        insert_rows.append((tuple(insert_cols), tuple(row)))

    grouped = {}
    for cols_tuple, vals in insert_rows:
        grouped.setdefault(cols_tuple, []).append(vals)

    for cols_tuple, vals_list in grouped.items():
        cols_join = ", ".join(cols_tuple)
        placeholders = ", ".join(["%s"] * len(cols_tuple))
        sql = f"INSERT INTO frequencia ({cols_join}) VALUES ({placeholders})"
        exec_many(cur, sql, vals_list)


def seed_exercicios(cur):
    nomes = [
        "Supino", "Agachamento", "Corrida", "Remada", "Elevação Lateral",
        "Prancha", "Burpee", "Levantamento Terra", "Avanço", "Flexão Barra"
    ]
    rows = [(n,) for n in nomes]
    exec_many(cur, "INSERT INTO exercicio (nome) VALUES (%s)", rows)
    cur.execute("SELECT id FROM exercicio")
    return [r[0] for r in cur.fetchall()]


def seed_treinos(cur, exercicios_ids, count=20):
    tipos = ["Cardio", "Peso"]
    rows = []
    for _ in range(count):
        ex_id = random.choice(exercicios_ids)
        tipo = random.choice(tipos)
        tempo = datetime.datetime.now() - datetime.timedelta(minutes=random.randint(10, 50))
        distancia = random.uniform(1.0, 10.0)
        repeticoes = random.randint(8, 15)
        peso = random.uniform(10.0, 60.0)
        series = random.randint(3, 5)
        recorde = peso + random.uniform(0, 10)
        rows.append((ex_id, tipo, tempo, distancia, repeticoes, peso, series, recorde))
    exec_many(cur, "INSERT INTO treino (exercicio_id, tipo, tempo, distancia, repeticoes, peso, series, recorde_carga) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)", rows)
    cur.execute("SELECT id FROM treino")
    return [r[0] for r in cur.fetchall()]


def seed_plano_treino(cur, perfis_info, treinos_ids, planos_por_user=1):
    estados = ["Ativo", "Historico"]
    dias_enum = ["Segunda", "Terca", "Quarta", "Quinta", "Sexta", "Sabado", "Domingo"]
    plano_ids = []
    for info in perfis_info:
        email = info["email"]
        for p in range(planos_por_user):
            nome = f"Plano {p+1} de {email}"
            estado = random.choice(estados)
            cur.execute("INSERT INTO plano_treino (usuario_email, nome, estado) VALUES (%s,%s,%s)", (email, nome, estado))
            plano_id = cur.lastrowid
            plano_ids.append(plano_id)
            # dias
            dias_pick = random.sample(dias_enum, k=random.randint(2, 4))
            exec_many(cur, "INSERT INTO plano_treino_dias (plano_id, dia) VALUES (%s,%s)", [(plano_id, d) for d in dias_pick])
            # vincular treinos
            treinos_pick = random.sample(treinos_ids, k=min(3, len(treinos_ids)))
            exec_many(cur, "INSERT INTO plano_treino_treinos (plano_id, treino_id) VALUES (%s,%s)", [(plano_id, t) for t in treinos_pick])
    return plano_ids


def seed_alimentos(cur):
    alimentos = [
        ("Frango", "Proteina", 165, 100.0),
        ("Arroz", "Carboidrato", 130, 100.0),
        ("Batata Doce", "Carboidrato", 86, 100.0),
        ("Abacate", "Gordura", 160, 100.0),
        ("Ovo", "Proteina", 155, 100.0),
        ("Azeite", "Gordura", 884, 100.0),
        ("Feijao", "Proteina", 127, 100.0),
        ("Macarrao", "Carboidrato", 131, 100.0),
    ]
    exec_many(cur, "INSERT INTO alimento (nome, categoria, calorias, gramas) VALUES (%s,%s,%s,%s)", alimentos)
    cur.execute("SELECT id FROM alimento")
    return [r[0] for r in cur.fetchall()]


def seed_refeicoes(cur, alimentos_ids, count=12):
    tipos = ["CafeDaManha", "Lanche", "Almoco", "Jantar"]
    refeicao_ids = []
    for _ in range(count):
        tipo = random.choice(tipos)
        cal = random.randint(300, 700)
        inicio = datetime.datetime.now() - datetime.timedelta(days=random.randint(0, 5))
        fim = inicio + datetime.timedelta(minutes=45)
        cur.execute(
            "INSERT INTO refeicao (tipo, calorias_totais, data_inicio, data_fim) VALUES (%s,%s,%s,%s)",
            (tipo, cal, inicio, fim),
        )
        rid = cur.lastrowid
        refeicao_ids.append(rid)
        picks = random.sample(alimentos_ids, k=min(3, len(alimentos_ids)))
        exec_many(cur, "INSERT INTO refeicao_alimentos (refeicao_id, alimento_id) VALUES (%s,%s)", [(rid, a) for a in picks])
    return refeicao_ids


def seed_plano_nutricional(cur, perfis_info, refeicao_ids, planos_por_user=1):
    objetivos = ["Cutting", "Bulking"]
    estados = ["Ativo", "Historico"]
    plano_ids = []
    for info in perfis_info:
        email = info["email"]
        for p in range(planos_por_user):
            objetivo = random.choice(objetivos)
            estado = random.choice(estados)
            cal_tot = random.randint(1800, 2800)
            cal_obj = cal_tot + random.randint(-200, 200)
            cur.execute(
                "INSERT INTO plano_nutricional (objetivo, estado, calorias_totais, calorias_objetivo, usuario_email) VALUES (%s,%s,%s,%s,%s)",
                (objetivo, estado, cal_tot, cal_obj, email),
            )
            pid = cur.lastrowid
            plano_ids.append(pid)
            refeicoes_pick = random.sample(refeicao_ids, k=min(4, len(refeicao_ids)))
            exec_many(cur, "INSERT INTO plano_refeicoes (plano_id, refeicao_id) VALUES (%s,%s)", [(pid, r) for r in refeicoes_pick])
    return plano_ids


def seed_acessorios(cur):
    data = [
        ("icone1", 100, "Bandana", "img1"),
        ("icone2", 200, "Luvas", "img2"),
        ("icone3", 300, "Cinto", "img3"),
        ("icone4", 150, "Munhequeira", "img4"),
    ]
    exec_many(cur, "INSERT INTO acessorio (icone, preco, nome, imagem) VALUES (%s,%s,%s,%s)", data)
    cur.execute("SELECT id FROM acessorio")
    return [r[0] for r in cur.fetchall()]


def vincular_avatar_acessorios(cur):
    # vincula alguns acessórios a avatares existentes, se tabela existir
    cur.execute("SHOW TABLES LIKE 'avatar_acessorios'")
    if not cur.fetchall():
        return
    cur.execute("SELECT id FROM avatar")
    avatars = [r[0] for r in cur.fetchall()]
    cur.execute("SELECT id FROM acessorio")
    acessorios = [r[0] for r in cur.fetchall()]
    rows = []
    for av in avatars:
        picks = random.sample(acessorios, k=min(2, len(acessorios))) if acessorios else []
        for ac in picks:
            rows.append((av, ac))
    exec_many(cur, "INSERT INTO avatar_acessorios (avatar_id, acessorio_id) VALUES (%s,%s)", rows)


def main():
    conn = connect()
    conn.autocommit = False
    try:
        cur = conn.cursor()
        assert_required_tables(cur)
        truncate_all(cur)
        perfis_info = seed_usuarios_perfis_avatares(cur, n=30)
        seed_amizades(cur, max_links=40)
        seed_equipes(cur, team_count=4)
        acessorios_ids = seed_acessorios(cur)
        vincular_avatar_acessorios(cur)
        exercicios_ids = seed_exercicios(cur)
        treinos_ids = seed_treinos(cur, exercicios_ids, count=25)
        seed_plano_treino(cur, perfis_info, treinos_ids, planos_por_user=1)
        alimentos_ids = seed_alimentos(cur)
        refeicoes_ids = seed_refeicoes(cur, alimentos_ids, count=15)
        seed_plano_nutricional(cur, perfis_info, refeicoes_ids, planos_por_user=1)
        seed_frequencia(cur, count=80)
        seed_feedback(cur, count=60)
        seed_conquistas(cur, count=12)
        conn.commit()
        print("Seed concluido com sucesso.")
    except Exception:
        conn.rollback()
        raise
    finally:
        conn.close()


if __name__ == "__main__":
    main()

