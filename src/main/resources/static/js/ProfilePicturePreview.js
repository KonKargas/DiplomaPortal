document.addEventListener("DOMContentLoaded", () => {

    const input =
        document.getElementById("profilePicture");

    const wrapper =
        document.querySelector(".profile-picture-wrapper");


    if (!input || !wrapper) {
        return;
    }


    input.addEventListener("change", () => {

        const file =
            input.files[0];


        if (!file) {
            return;
        }


        const reader =
            new FileReader();


        reader.addEventListener("load", event => {

            let preview =
                document.getElementById("profilePreview");

            const placeholder =
                document.getElementById("profilePlaceholder");


            /*
             * If there is no saved profile picture,
             * create the preview image.
             */
            if (!preview) {

                preview =
                    document.createElement("img");

                preview.id =
                    "profilePreview";

                preview.className =
                    "profile-picture";

                preview.alt =
                    "Profile picture preview";

                wrapper.appendChild(
                    preview
                );
            }


            preview.src =
                event.target.result;


            /*
             * Hide the initial placeholder.
             *
             * We use display:none instead of the
             * hidden attribute because the shared
             * CSS explicitly sets display:flex
             * on the placeholder.
             */
            if (placeholder) {

                placeholder.style.display =
                    "none";
            }

        });


        reader.readAsDataURL(
            file
        );

    });

});