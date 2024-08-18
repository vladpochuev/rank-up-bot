const checkboxes = document.getElementsByTagName('input')
for (let checkbox of checkboxes) {
    checkbox.addEventListener('change', () => {
        const textareas = document.getElementsByTagName('textarea')
        const textarea = Array.from(textareas).filter((el) => el.className === checkbox.className)[0]
        textarea.disabled = !checkbox.checked;
    })
}