(() => {
    const selector = '.btn, .pressable';
    let activeElement = null;

    function clearPressedState() {
        if (!activeElement) {
            return;
        }

        activeElement.classList.remove('is-pressed');
        activeElement = null;
    }

    function setPressedState(target) {
        const element = target.closest(selector);
        if (!element) {
            clearPressedState();
            return;
        }

        if (activeElement && activeElement !== element) {
            activeElement.classList.remove('is-pressed');
        }

        activeElement = element;
        activeElement.classList.add('is-pressed');
    }

    document.addEventListener('pointerdown', (event) => {
        setPressedState(event.target);
    });

    document.addEventListener('pointerup', clearPressedState);
    document.addEventListener('pointercancel', clearPressedState);
    document.addEventListener('dragstart', clearPressedState);
    window.addEventListener('blur', clearPressedState);
})();
