// YASUKETV - Sistema de Navegação e Efeitos Visuais
// Versão focada em conteúdo sem sistema de edição

// Inicializar quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Configurar navegação suave
    setupSmoothNavigation();
    
    // Configurar sistema de revelação ao scroll
    setupScrollReveal();
    
    // Configurar modal de vídeos do YouTube
    setupVideoModal();
    
    console.log('🎌 YASUKETV Pergaminho - Carregado!');
}

// Sistema de Navegação Suave
function setupSmoothNavigation() {
    document.querySelectorAll('.nav-scroll').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            
            // Verificar se é um link interno
            if (targetId.startsWith('#')) {
                const targetElement = document.querySelector(targetId);
                
                if (targetElement) {
                    window.scrollTo({
                        top: targetElement.offsetTop - 20,
                        behavior: 'smooth'
                    });
                }
            } else {
                // Link externo - comportamento normal
                window.location.href = targetId;
            }
        });
    });
}

// Sistema de Revelação ao Scroll
function setupScrollReveal() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
                
                // Adicionar delay progressivo para itens de lista
                if (entry.target.classList.contains('scroll-item')) {
                    const items = Array.from(entry.target.parentElement.children);
                    const index = items.indexOf(entry.target);
                    entry.target.style.transitionDelay = `${index * 0.1}s`;
                }
            }
        });
    }, observerOptions);

    // Aplicar efeito aos elementos
    document.querySelectorAll('.scroll-item, .youtube-card, .gallery-item, .criterion').forEach(item => {
        item.style.opacity = '0';
        item.style.transform = 'translateY(30px)';
        item.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(item);
    });
}

// Sistema de Modal para Vídeos do YouTube
function setupVideoModal() {
    const videoModal = document.getElementById('videoModal');
    const videoContainer = document.getElementById('videoContainer');
    const closeModal = document.getElementById('closeModal');

    // Mapeamento de vídeos do YouTube (substitua com seus IDs reais)
    const videoMap = {
        'hunter-x-hunter-analysis': 'dQw4w9WgXcQ', // Exemplo - substitua
        'berserk-analysis': 'dQw4w9WgXcQ',        // Exemplo - substitua
        'one-piece-analysis': 'dQw4w9WgXcQ',      // Exemplo - substitua
        'monster-analysis': 'dQw4w9WgXcQ',        // Exemplo - substitua
        'aot-analysis': 'dQw4w9WgXcQ'            // Exemplo - substitua
    };

    document.querySelectorAll('.youtube-card').forEach(card => {
        card.addEventListener('click', function() {
            const videoKey = this.getAttribute('data-video-key');
            const videoId = videoMap[videoKey];
            
            if (videoId) {
                videoContainer.innerHTML = `
                    <iframe 
                        src="https://www.youtube.com/embed/${videoId}?autoplay=1&rel=0" 
                        frameborder="0" 
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                        allowfullscreen
                        title="Vídeo do YouTube - YASUKETV">
                    </iframe>
                `;
                videoModal.classList.add('active');
                document.body.style.overflow = 'hidden';
            } else {
                console.warn('ID do vídeo não encontrado para:', videoKey);
            }
        });
    });

    // Fechar modal
    if (closeModal) {
        closeModal.addEventListener('click', closeVideoModal);
    }

    // Fechar modal ao clicar fora
    if (videoModal) {
        videoModal.addEventListener('click', function(e) {
            if (e.target === videoModal) {
                closeVideoModal();
            }
        });
    }

    // Fechar modal com tecla ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && videoModal.classList.contains('active')) {
            closeVideoModal();
        }
    });

    function closeVideoModal() {
        videoModal.classList.remove('active');
        videoContainer.innerHTML = '';
        document.body.style.overflow = 'auto';
    }
}

// Sistema de Filtros Simples (opcional - para futuras expansões)
function setupSimpleFilters() {
    const filterButtons = document.querySelectorAll('[data-filter]');
    
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            const filterType = this.getAttribute('data-filter');
            const filterValue = this.getAttribute('data-value');
            
            applyFilter(filterType, filterValue);
            
            // Atualizar botões ativos
            filterButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
        });
    });
}

function applyFilter(type, value) {
    // Implementação básica de filtro - pode ser expandida conforme necessidade
    console.log(`Aplicando filtro: ${type} = ${value}`);
    
    // Exemplo: Filtrar por gênero
    if (type === 'genre') {
        document.querySelectorAll('.scroll-item').forEach(item => {
            const genres = item.getAttribute('data-genres');
            if (value === 'all' || (genres && genres.includes(value))) {
                item.style.display = 'grid';
            } else {
                item.style.display = 'none';
            }
        });
    }
}

// Sistema de Busca Rápida (opcional)
function setupQuickSearch() {
    const searchInput = document.getElementById('searchInput');
    
    if (searchInput) {
        searchInput.addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            
            document.querySelectorAll('.scroll-item').forEach(item => {
                const title = item.querySelector('.anime-title').textContent.toLowerCase();
                const description = item.querySelector('.anime-description').textContent.toLowerCase();
                
                if (title.includes(searchTerm) || description.includes(searchTerm)) {
                    item.style.display = 'grid';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    }
}

// Efeitos de Hover Avançados
function setupHoverEffects() {
    // Efeito de brilho nos cards do YouTube
    document.querySelectorAll('.youtube-card').forEach(card => {
        card.addEventListener('mousemove', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            this.style.setProperty('--mouse-x', `${x}px`);
            this.style.setProperty('--mouse-y', `${y}px`);
        });
    });
}

// Carregamento Progressivo de Imagens
function setupProgressiveLoading() {
    const lazyImages = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.getAttribute('data-src');
                img.removeAttribute('data-src');
                imageObserver.unobserve(img);
            }
        });
    });

    lazyImages.forEach(img => imageObserver.observe(img));
}

// Inicializar funcionalidades adicionais se necessário
function setupAdditionalFeatures() {
    // Configurar tooltips
    setupTooltips();
    
    // Configurar contador de visualizações (simulado)
    setupViewCounters();
}

function setupTooltips() {
    const elementsWithTooltip = document.querySelectorAll('[data-tooltip]');
    
    elementsWithTooltip.forEach(element => {
        element.addEventListener('mouseenter', showTooltip);
        element.addEventListener('mouseleave', hideTooltip);
    });
    
    function showTooltip(e) {
        const tooltipText = this.getAttribute('data-tooltip');
        const tooltip = document.createElement('div');
        tooltip.className = 'tooltip';
        tooltip.textContent = tooltipText;
        tooltip.style.cssText = `
            position: absolute;
            background: var(--blood);
            color: var(--parchment);
            padding: 0.5rem 1rem;
            border-radius: 4px;
            font-size: 0.8rem;
            z-index: 1000;
            white-space: nowrap;
            pointer-events: none;
        `;
        
        document.body.appendChild(tooltip);
        
        const rect = this.getBoundingClientRect();
        tooltip.style.left = `${rect.left + window.scrollX}px`;
        tooltip.style.top = `${rect.top + window.scrollY - tooltip.offsetHeight - 10}px`;
        
        this._currentTooltip = tooltip;
    }
    
    function hideTooltip() {
        if (this._currentTooltip) {
            this._currentTooltip.remove();
            this._currentTooltip = null;
        }
    }
}

function setupViewCounters() {
    // Simular contadores de visualização (para demonstração)
    document.querySelectorAll('.youtube-card').forEach((card, index) => {
        const viewsElement = card.querySelector('.view-count');
        if (viewsElement) {
            const baseViews = 1000 + (index * 500);
            const randomViews = Math.floor(Math.random() * 2000);
            const totalViews = baseViews + randomViews;
            viewsElement.textContent = formatViewCount(totalViews);
        }
    });
}

function formatViewCount(count) {
    if (count >= 1000000) {
        return (count / 1000000).toFixed(1) + 'M views';
    } else if (count >= 1000) {
        return (count / 1000).toFixed(1) + 'K views';
    }
    return count + ' views';
}

// Adicionar CSS para animações
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes fadeInUp {
        from { 
            opacity: 0;
            transform: translateY(30px);
        }
        to { 
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .tooltip {
        animation: fadeInUp 0.2s ease-out;
    }
    
    /* Efeito de brilho nos cards do YouTube */
    .youtube-card {
        position: relative;
        overflow: hidden;
    }
    
    .youtube-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: radial-gradient(
            600px circle at var(--mouse-x) var(--mouse-y),
            rgba(139, 0, 0, 0.1),
            transparent 40%
        );
        opacity: 0;
        transition: opacity 0.3s ease;
        pointer-events: none;
    }
    
    .youtube-card:hover::before {
        opacity: 1;
    }
`;
document.head.appendChild(style);