document.addEventListener("DOMContentLoaded", () => {
    const inputs = [
        document.getElementById("implementationGrade"),
        document.getElementById("reportGrade"),
        document.getElementById("presentationGrade")
    ];

    const preview = document.querySelector("#finalGradePreview span");
    if (inputs.some(input => !input) || !preview) return;

    const calculateFinalGrade = () => {
        const values = inputs.map(input => Number.parseFloat(input.value));
        if (values.some(Number.isNaN)) {
            preview.textContent = "-";
            return;
        }

        const average = values.reduce((sum, value) => sum + value, 0) / values.length;
        preview.textContent = average.toFixed(2);
    };

    inputs.forEach(input => input.addEventListener("input", calculateFinalGrade));
    calculateFinalGrade();
});
