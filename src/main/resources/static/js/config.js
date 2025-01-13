function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

function send() {
    const form = document.querySelector("#form")
    if (validateForms()) {
        form.submit()
    } else {
        scrollToTop()
    }
}

const levelsTextArea = document.querySelector("#levels_textarea")
const ranksTextArea = document.querySelector("#ranks_textarea")

levelsTextArea.addEventListener("input", () => {
    const customRanksCheckbox = document.querySelector("#custom_ranks_checkbox")

    if (!customRanksCheckbox.checked) {
        let levelsNumber = levelsTextArea.value.split(", ").length
        if (levelsTextArea.value.endsWith(",")) levelsNumber++;
        if (levelsTextArea.value === "") levelsNumber = 0

        ranksTextArea.value = getNValues(ranksTextArea.dataset.default, levelsNumber, "\n")
    }
})

ranksTextArea.addEventListener("input", () => {
    const customLevelsCheckbox = document.querySelector("#custom_levels_checkbox")

    if (!customLevelsCheckbox.checked) {
        let ranksNumber = ranksTextArea.value.split("\n").length
        if (ranksTextArea.value.endsWith("\n")) ranksNumber--;
        if (ranksTextArea.value === "") ranksNumber = 0

        levelsTextArea.value = getNValues(levelsTextArea.dataset.default, ranksNumber, ", ")
    }
})

function getNValues(sequence, n, delimiter) {
    const split = sequence.split(delimiter)
    let result = ""
    for (let i = 0; i < n; i++) {
        let value = split[i]
        result = result + value + (i === n - 1 ? "" : delimiter)
    }
    return result
}

const checkboxes = document.getElementsByTagName("input")
for (let checkbox of checkboxes) {
    checkbox.addEventListener("change", () => {
        const textAreas = document.getElementsByTagName("textarea")
        const textArea = Array.from(textAreas).filter((el) => el.className === checkbox.className)[0]
        textArea.disabled = !checkbox.checked

        if (checkbox.checked) {
            textArea.value = textArea.dataset.custom
        } else {
            textArea.dataset.custom = textArea.value
            textArea.value = textArea.dataset.default

            if (textArea === ranksTextArea) {
                levelsTextArea.dispatchEvent(new Event("input", {bubbles: true}))
            } else {
                ranksTextArea.dispatchEvent(new Event("input", {bubbles: true}))
            }
        }
    })
}
