document.addEventListener("DOMContentLoaded", () => {
    const thresholdFields = document.getElementById("thresholdFields");
    if (!thresholdFields) return;

    const strategyRadios = document.querySelectorAll('input[name="strategyType"]');
    const gradeInput = document.getElementById("gradeThreshold");
    const coursesInput = document.getElementById("coursesThreshold");

    const updateThresholdVisibility = () => {
        const selected = document.querySelector('input[name="strategyType"]:checked');
        const isThreshold = selected?.value === "THRESHOLD";

        thresholdFields.classList.toggle("visible", isThreshold);
        if (gradeInput) gradeInput.required = isThreshold;
        if (coursesInput) coursesInput.required = isThreshold;
    };

    strategyRadios.forEach(radio =>
        radio.addEventListener("change", updateThresholdVisibility)
    );

    updateThresholdVisibility();
});
