// script.js - Versão Mobile-Optimized

// =============================================
// 1. DETECÇÃO DE MOBILE E TOUCH
// =============================================
const isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
const isTouchDevice = 'ontouchstart' in window || navigator.maxTouchPoints > 0;

// =============================================
// 2. BARRA DE PROGRESSO OTIMIZADA
// =============================================
const progressBar = document.getElementById('progressBar');

function updateProgressBar() {
    const winHeight = window.innerHeight;
    const docHeight = document.documentElement.scrollHeight - winHeight;
    const scrolled = (window.scrollY / docHeight) * 100;
    progressBar.style.width = Math.min(100, Math.max(0, scrolled)) + '%';
}

// Throttle para performance mobile
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    }
}

window.addEventListener('scroll', throttle(updateProgressBar, 16)); // ~60fps

// =============================================
// 3. BOTÃO VOLTAR AO TOPO MOBILE
// =============================================
const topBtn = document.getElementById('topBtn');

function toggleTopButton() {
    if (window.pageYOffset > 200) { // Menor threshold no mobile
        topBtn.classList.add('show');
    } else {
        topBtn.classList.remove('show');
    }
}

window.addEventListener('scroll', throttle(toggleTopButton, 100));

topBtn.addEventListener('click', (e) => {
    e.preventDefault();
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

// Touch feedback
if (isTouchDevice) {
    topBtn.addEventListener('touchstart', function() {
        this.style.transform = 'scale(0.95)';
    });
    
    topBtn.addEventListener('touchend', function() {
        this.style.transform = '';
    });
}

// =============================================
// 4. SISTEMA DE TEMAS MOBILE
// =============================================
const themeBtn = document.getElementById('themeBtn');
const currentTheme = localStorage.getItem('theme') || 'dark';

function applyTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    themeBtn.textContent = theme === 'light' ? '☀️' : '🌙';
    localStorage.setItem('theme', theme);
    
    // Atualizar meta theme-color para mobile
    const themeColor = theme === 'light' ? '#f5f2e9' : '#07060a';
    document.querySelector('meta[name="theme-color"]')?.setAttribute('content', themeColor);
}

// Aplicar tema inicial
applyTheme(currentTheme);

// Evento otimizado para touch
const themeHandler = isTouchDevice ? 'touchend' : 'click';
themeBtn.addEventListener(themeHandler, (e) => {
    if (isTouchDevice) e.preventDefault();
    const newTheme = document.documentElement.getAttribute('data-theme') === 'light' ? 'dark' : 'light';
    applyTheme(newTheme);
});

// =============================================
// 5. SCROLL SUAVE MOBILE
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
                const targetPosition = target.offsetTop - headerHeight - 10; // Menor offset no mobile
                
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
                
                // Feedback tátil
                if (isTouchDevice) {
                    this.style.backgroundColor = 'rgba(230, 192, 123, 0.2)';
                    setTimeout(() => {
                        this.style.backgroundColor = '';
                    }, 300);
                }
            }
        });
    });
}

// =============================================
// 6. TOC INTERATIVO MOBILE
// =============================================
const sections = document.querySelectorAll('article[id], section[id]');
const navLinks = document.querySelectorAll('.toc a');

function highlightCurrentSection() {
    let current = '';
    const scrollPosition = window.scrollY + (window.innerHeight * 0.3); // Ajuste mobile
    
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
            
            // Scroll suave do TOC no mobile
            if (isMobile && link.parentElement) {
                link.parentElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'nearest'
                });
            }
        }
    });
}

// =============================================
// 7. OTIMIZAÇÕES DE PERFORMANCE MOBILE
// =============================================
function initPerformanceOptimizations() {
    // Prevenir múltiplos resizes
    let resizeTimeout;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(() => {
            updateProgressBar();
            toggleTopButton();
        }, 250);
    });
    
    // Load crítico primeiro
    const criticalImages = document.querySelectorAll('img[data-src]');
    criticalImages.forEach(img => {
        if (img.getBoundingClientRect().top < window.innerHeight * 2) {
            img.src = img.dataset.src;
            img.classList.remove('lazy');
        }
    });
}

// =============================================
// 8. GESTOS TOUCH
// =============================================
function initTouchGestures() {
    if (!isTouchDevice) return;
    
    let startY;
    
    document.addEventListener('touchstart', (e) => {
        startY = e.touches[0].clientY;
    }, { passive: true });
    
    // Swipe down para mostrar botão topo
    document.addEventListener('touchend', (e) => {
        if (!startY) return;
        
        const endY = e.changedTouches[0].clientY;
        const diff = startY - endY;
        
        if (diff < -50 && window.pageYOffset > 100) { // Swipe para baixo
            topBtn.classList.add('show');
        }
        
        startY = null;
    }, { passive: true });
}

// =============================================
// 9. INICIALIZAÇÃO MOBILE
// =============================================
document.addEventListener('DOMContentLoaded', () => {
    // Atualizar ano
    document.getElementById('year').textContent = new Date().getFullYear();
    
    // Inicializar componentes
    initSmoothScroll();
    initPerformanceOptimizations();
    initTouchGestures();
    
    // Inicializar highlight do TOC
    highlightCurrentSection();
    window.addEventListener('scroll', throttle(highlightCurrentSection, 50));
    
    // Ajustes específicos para mobile
    if (isMobile) {
        // Remover animações pesadas em dispositivos lentos
        const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
        if (reducedMotion) {
            document.querySelectorAll('article').forEach(article => {
                article.style.animation = 'none';
                article.style.opacity = '1';
                article.style.transform = 'none';
            });
        }
        
        // Otimizar vídeos
        const videos = document.querySelectorAll('iframe');
        videos.forEach(video => {
            video.setAttribute('loading', 'lazy');
        });
    }
    
    console.log('📱 Elliot Project - Mobile optimized scripts loaded!');
});

// =============================================
// 10. OFFLINE SUPPORT
// =============================================
// Service Worker simples para cache
if ('serviceWorker' in navigator && isMobile) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/sw.js').catch(console.error);
    });
}