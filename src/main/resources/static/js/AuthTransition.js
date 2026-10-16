document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("authContainer");
    if (!container) return;

    const bindTransition = (linkId, transitionClass) => {
        const link = document.getElementById(linkId);
        if (!link) return;

        link.addEventListener("click", event => {
            event.preventDefault();
            container.classList.add(transitionClass);
            setTimeout(() => {
                window.location.href = link.href;
            }, 400);
        });
    };

    bindTransition("registerLink", "transition-to-register");
    bindTransition("loginLink", "transition-to-login");
});
