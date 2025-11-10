// script.js - Todas as funcionalidades JavaScript do projeto Elliot

// =============================================
// 1. BARRA DE PROGRESSO DE LEITURA
// =============================================
const progressBar = document.getElementById('progressBar');

window.addEventListener('scroll', () => {
    const winHeight = window.innerHeight;
    const docHeight = document.documentElement.scrollHeight - winHeight;
    const scrolled = (window.scrollY / docHeight) * 100;
    progressBar.style.width = scrolled + '%';
});

// =============================================
// 2. BOTÃO VOLTAR AO TOPO
// =============================================
const topBtn = document.getElementById('topBtn');

window.addEventListener('scroll', () => {
    if (window.pageYOffset > 300) {
        topBtn.classList.add('show');
    } else {
        topBtn.classList.remove('show');
    }
});

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
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// =============================================
// 5. ATUALIZAR ANO NO FOOTER
// =============================================
document.getElementById('year').textContent = new Date().getFullYear();

// =============================================
// 6. DESTACAR ITEM ATUAL NO TOC
// =============================================
const sections = document.querySelectorAll('article, section[id]');
const navLinks = document.querySelectorAll('.toc a');

function highlightCurrentSection() {
    let current = '';
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.clientHeight;
        if (scrollY >= (sectionTop - 100)) {
            current = section.getAttribute('id');
        }
    });

    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === `#${current}`) {
            link.classList.add('active');
            link.style.color = 'var(--accent)';
        } else {
            link.style.color = '';
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

window.addEventListener('scroll', debounce(highlightCurrentSection, 10));

// =============================================
// 7. ANIMAÇÃO DE DIGITAÇÃO PARA TEXTO DESTACADO (Opcional)
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

// Inicializar animação de digitação no hero (opcional)
document.addEventListener('DOMContentLoaded', () => {
    const heroTitle = document.getElementById('hero-title');
    if (heroTitle) {
        const originalText = heroTitle.textContent;
        typeWriter(heroTitle, originalText, 70);
    }
});

// =============================================
// 8. LAZY LOADING PARA IMAGENS FUTURAS
// =============================================
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

console.log('🚀 Elliot Project - Scripts carregados com sucesso!');