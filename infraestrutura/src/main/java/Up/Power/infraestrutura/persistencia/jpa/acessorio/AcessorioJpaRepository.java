import org.springframework.data.jpa.repository.JpaRepository;

interface AcessorioJpaRepository extends JpaRepository<AcessorioJpa, Integer> {
    // Aqui você pode criar consultas personalizadas se quiser
}
