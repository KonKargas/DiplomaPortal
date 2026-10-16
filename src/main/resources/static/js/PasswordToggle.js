document.addEventListener("DOMContentLoaded", function () {

    const toggleButtons =
        document.querySelectorAll(".password-toggle");


    toggleButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const targetId =
                button.getAttribute("data-target");


            const input =
                document.getElementById(targetId);


            if (!input) {
                return;
            }


            const passwordIsHidden =
                input.type === "password";


            input.type =
                passwordIsHidden
                    ? "text"
                    : "password";


            button.classList.toggle(
                "is-visible",
                passwordIsHidden
            );


            button.setAttribute(
                "aria-label",
                passwordIsHidden
                    ? "Hide password"
                    : "Show password"
            );

        });

    });

});