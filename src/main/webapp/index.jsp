<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cabinet Dentaire - Soins dentaires à Rabat</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homepage.css">
</head>
<body>

<!-- Navigation -->
<nav class="navbar">
    <div class="nav-container">
        <a href="/" class="logo"><i class="fas fa-building"></i> Cabinet Dentaire</a>
        <ul class="nav-menu">
            <li><a href="#accueil">Accueil</a></li>
            <li><a href="#services">Services</a></li>
            <li><a href="#medecins">Médecins</a></li>
            <li><a href="#contact">Contact</a></li>
        </ul>
    </div>
</nav>

<!-- Hero Section -->
<section class="hero" id="accueil">
    <div class="hero-content">
        <div class="hero-text">
            <h1>Votre sourire, <span class="highlight">notre passion</span></h1>
            <p>Soins dentaires modernes et personnalisés à Rabat</p>
            <div class="hero-buttons">
                <a href="public-rdv" class="btn-primary"><i class="fas fa-calendar-check"></i> Prendre RDV</a>
                <a href="#services" class="btn-secondary"><i class="fas fa-chevron-right"></i> En Savoir Plus</a>
            </div>
        </div>
        <div class="hero-image">
            <i class="fas fa-tooth floating-tooth"></i>
        </div>
    </div>
</section>

<!-- Services Section -->
<section class="services" id="services">
    <h2>Nos Soins Dentaires</h2>
    <div class="services-grid">
        <div class="service-card">
            <i class="fas fa-tooth service-icon"></i>
            <h3>Détartrage</h3>
            <p>Nettoyage professionnel pour des dents saines</p>
        </div>
        <div class="service-card">
            <i class="fas fa-star service-icon"></i>
            <h3>Blanchiment</h3>
            <p>Sourire éclatant en une séance</p>
        </div>
        <div class="service-card">
            <i class="fas fa-gem service-icon"></i>
            <h3>Implants</h3>
            <p>Solutions durables pour remplacer vos dents</p>
        </div>
        <div class="service-card">
            <i class="fas fa-child service-icon"></i>
            <h3>Pédodontie</h3>
            <p>Soins dentaires pour enfants</p>
        </div>
        <div class="service-card">
            <i class="fas fa-smile service-icon"></i>
            <h3>Orthodontie</h3>
            <p>Bagues et aligneurs transparents</p>
        </div>
        <div class="service-card">
            <i class="fas fa-syringe service-icon"></i>
            <h3>Chirurgie</h3>
            <p>Extraction et chirurgie buccale</p>
        </div>
    </div>
</section>

<!-- Doctors Section  -->
<section class="team-section" id="medecins">
    <div class="team-container">
        <div class="team-header">
            <span class="team-badge">Experts en dentisterie</span>
            <h2>Notre Équipe</h2>
            <p>Des professionnels dédiés à votre santé bucco-dentaire</p>
        </div>
        <div class="team-grid">
            <div class="team-card">
                <div class="team-image">
                    <i class="fas fa-user-md"></i>
                </div>
                <div class="team-info">
                    <h3>Dr. Jihane</h3>
                    <p class="specialty">Chirurgien-dentiste</p>
                    <p class="exp">15 ans d'expérience</p>
                </div>
            </div>
            <div class="team-card">
                <div class="team-image">
                    <i class="fas fa-user-md"></i>
                </div>
                <div class="team-info">
                    <h3>Dr. Omar bourrehane</h3>
                    <p class="specialty">Orthodontiste</p>
                    <p class="exp">10 ans d'expérience</p>
                </div>
            </div>
            <div class="team-card">
                <div class="team-image">
                    <i class="fas fa-user-md"></i>
                </div>
                <div class="team-info">
                    <h3>Dr. Anouar Ennaciri</h3>
                    <p class="specialty">Pédodontiste</p>
                    <p class="exp">8 ans d'expérience</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Contact Section -->
<section class="contact" id="contact">
    <h2>Contactez-nous</h2>
    <div class="contact-grid">
        <div class="contact-info">
            <p><i class="fas fa-map-marker-alt"></i> Emsi agdal 2 Groupe 12 Anouar, Rabat</p>
            <p><i class="fas fa-phone"></i> 07 66 88 99 25</p>
            <p><i class="fas fa-envelope"></i> anouar.pitech@gmail.com</p>
            <p><i class="fas fa-clock"></i> Lun-Ven: 9h-19h | Sam: 9h-14h</p>
        </div>
        <div class="contact-form">
            <form>
                <input type="text" placeholder="Nom complet" />
                <input type="email" placeholder="Email" />
                <textarea placeholder="Message" rows="4"></textarea>
                <button type="submit" class="btn-primary">Envoyer</button>
            </form>
        </div>
    </div>
</section>

<footer>
    <div class="footer-content">
        <p>&copy; 2026 Cabinet Dentaire - Tous droits réservés</p>
        <div class="footer-staff">
            <a href="login" class="staff-link">Espace Staff</a>
        </div>
    </div>
</footer>

</body>
</html>