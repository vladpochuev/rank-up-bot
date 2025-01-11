const checkboxes = document.getElementsByTagName("input")
for (let checkbox of checkboxes) {
    checkbox.addEventListener("change", () => {
        const textAreas = document.getElementsByTagName("textarea")
        const textArea = Array.from(textAreas).filter((el) => el.className === checkbox.className)[0]
        textArea.disabled = !checkbox.checked
    })
}

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