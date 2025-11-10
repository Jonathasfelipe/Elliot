// script.js - Todas as funcionalidades JavaScript do projeto Elliot

// =============================================
// 1. BARRA DE PROGRESSO DE LEITURA
// =============================================
const progressBar = document.getElementById('progressBar');

function updateProgressBar() {
    const winHeight = window.innerHeight;
    const docHeight = document.documentElement.scrollHeight - winHeight;
    const scrolled = (window.scrollY / docHeight) * 100;
    progressBar.style.width = scrolled + '%';
}

window.addEventListener('scroll', updateProgressBar);

// =============================================
// 2. BOTÃO VOLTAR AO TOPO
// =============================================
const topBtn = document.getElementById('topBtn');

function toggleTopButton() {
    if (window.pageYOffset > 300) {
        topBtn.classList.add('show');
    } else {
        topBtn.classList.remove('show');
    }
}

window.addEventListener('scroll', toggleTopButton);

topBtn.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

// =============================================
// 3. SISTEMA DE TEMAS (Claro/Escuro)
// =============================================
const themeBtn = document.getElementById('themeBtn');
const currentTheme = localStorage.getItem('theme') || 'dark';

// Aplicar tema salvo ao carregar
function applyTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    themeBtn.textContent = theme === 'light' ? '☀️' : '🌙';
    localStorage.setItem('theme', theme);
}

// Aplicar tema inicial
applyTheme(currentTheme);

// Alternar tema ao clicar
themeBtn.addEventListener('click', () => {
    const newTheme = document.documentElement.getAttribute('data-theme') === 'light' ? 'dark' : 'light';
    applyTheme(newTheme);
});

// =============================================
// 4. ANIMAÇÃO DE SCROLL SUAVE PARA LINKS INTERNOS
// =============================================
function initSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const href = this.getAttribute('href');
            if (href === '#') return;
            
            e.preventDefault();
            const target = document.querySelector(href);
            if (target) {
                const headerHeight = document.querySelector('header').offsetHeight;
                const targetPosition = target.offsetTop - headerHeight - 20;
                
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        });
    });
}

// =============================================
// 5. ATUALIZAR ANO NO FOOTER
// =============================================
function updateYear() {
    document.getElementById('year').textContent = new Date().getFullYear();
}

// =============================================
// 6. DESTACAR ITEM ATUAL NO TOC
// =============================================
const sections = document.querySelectorAll('article[id], section[id]');
const navLinks = document.querySelectorAll('.toc a');

function highlightCurrentSection() {
    let current = '';
    const scrollPosition = window.scrollY + 100;
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.clientHeight;
        if (scrollPosition >= sectionTop && scrollPosition < sectionTop + sectionHeight) {
            current = section.getAttribute('id');
        }
    });

    navLinks.forEach(link => {
        link.classList.remove('active');
        const href = link.getAttribute('href').substring(1);
        if (href === current) {
            link.classList.add('active');
        }
    });
}

// Debounce function para melhor performance
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// =============================================
// 7. ANIMAÇÃO DE DIGITAÇÃO PARA TEXTO DESTACADO
// =============================================
function typeWriter(element, text, speed = 50) {
    let i = 0;
    element.innerHTML = '';
    
    function type() {
        if (i < text.length) {
            element.innerHTML += text.charAt(i);
            i++;
            setTimeout(type, speed);
        }
    }
    type();
}

// =============================================
// 8. LAZY LOADING PARA IMAGENS FUTURAS
// =============================================
function initLazyLoading() {
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        document.querySelectorAll('img[data-src]').forEach(img => {
            imageObserver.observe(img);
        });
    }
}

// =============================================
// 9. INICIALIZAÇÃO GERAL
// =============================================
document.addEventListener('DOMContentLoaded', () => {
    // Atualizar ano
    updateYear();
    
    // Inicializar scroll suave
    initSmoothScroll();
    
    // Inicializar lazy loading
    initLazyLoading();
    
    // Inicializar highlight do TOC
    highlightCurrentSection();
    window.addEventListener('scroll', debounce(highlightCurrentSection, 10));
    
    // Animação de digitação opcional no hero
    const heroTitle = document.getElementById('hero-title');
    if (heroTitle && window.innerWidth > 768) {
        const originalText = heroTitle.textContent;
        setTimeout(() => {
            typeWriter(heroTitle, originalText, 70);
        }, 1000);
    }
    
    console.log('🚀 Elliot Project - Scripts carregados com sucesso!');
});

// =============================================
// 10. OTIMIZAÇÕES DE PERFORMANCE
// =============================================
// Prevenir múltiplos event listeners
let resizeTimeout;
window.addEventListener('resize', () => {
    clearTimeout(resizeTimeout);
    resizeTimeout = setTimeout(() => {
        updateProgressBar();
        toggleTopButton();
    }, 250);
});