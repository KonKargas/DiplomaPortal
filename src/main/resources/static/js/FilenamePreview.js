document.addEventListener("DOMContentLoaded", () => {
    const bindFilenamePreview = (inputId, outputId) => {
        const input = document.getElementById(inputId);
        const output = document.getElementById(outputId);
        if (!input || !output) return;

        input.addEventListener("change", () => {
            output.textContent = input.files.length > 0
                ? input.files[0].name
                : "No file selected";
        });
    };

    bindFilenamePreview("pdfFile", "selectedFileName");
    bindFilenamePreview("replacementPdfFile", "replacementFileName");
});
