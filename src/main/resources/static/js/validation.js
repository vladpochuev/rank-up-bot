const levels = document.querySelector("#levels_textarea")
const ranks = document.querySelector("#ranks_textarea")
const images = document.querySelector("#images_textarea")

function validateLevels() {
    const customLevelsCheckbox = document.querySelector("#custom_levels_checkbox")
    if (!customLevelsCheckbox.checked) return true

    const levelsError = document.querySelector("#levels_error")
    const regex = /^\s*(\d+\s*(,\s*\d+\s*)*)?$/

    const levelsValue = levels.value.trim()
    if (levelsValue === "" || levelsValue.match(regex) == null) {
        levelsError.style.display = "block"
        return false
    } else {
        levelsError.style.display = "none"
        return true
    }
}

function validateRanks() {
    const customRanksCheckbox = document.querySelector("#custom_ranks_checkbox")
    if (!customRanksCheckbox.checked) return true

    const ranksError = document.querySelector("#ranks_error")
    const regex = /^\s*((\S+(\s+\S+)*)\s*(\n\s*(\S+(\s+\S+)*)\s*)*)?$/

    const ranksValue = ranks.value.trim()
    if (ranksValue === "" || ranksValue.match(regex) == null) {
        ranksError.style.display = "block"
        return false
    } else {
        ranksError.style.display = "none"
        return true
    }
}

function validateImages() {
    const imagesError = document.querySelector("#images_error")
    const regex = /^\s*(https?:\/\/\S+\s*(\n\s*https?:\/\/\S+\s*)*)?$/
    if (images.value.trim().match(regex) == null) {
        imagesError.style.display = "block"
        return false
    } else {
        imagesError.style.display = "none"
        return true
    }
}

function validateQuantity() {
    const levelsValue = levels.value.trim()
    const ranksValue = ranks.value.trim()

    const levelsQuantity = levelsValue === "" ? 0 : levelsValue.split(",").length
    const ranksQuantity = ranksValue === "" ? 0 : ranksValue.split("\n").length

    const quantityError = document.querySelector("#quantity_error")
    if (ranksQuantity !== levelsQuantity) {
        quantityError.style.display = "block"
        return false
    } else {
        quantityError.style.display = "none"
        return true
    }
}

function validateDefaultLevelsOverflow() {
    const customLevelCheckbox = document.querySelector("#custom_levels_checkbox")
    const overflowError = document.querySelector("#default_levels_overflow_error")
    if (!customLevelCheckbox.checked && levels.value.length > levels.dataset.default.length) {
        overflowError.style.display = "block"
        return false
    } else {
        overflowError.style.display = "none"
        return true
    }
}

function validateDefaultRanksOverflow() {
    const customRanksCheckbox = document.querySelector("#custom_ranks_checkbox")
    const overflowError = document.querySelector("#default_ranks_overflow_error")
    if (!customRanksCheckbox.checked && ranks.value.length > ranks.dataset.default.length) {
        overflowError.style.display = "block"
        return false
    } else {
        overflowError.style.display = "none"
        return true
    }
}

function validateForms() {
    return validateLevels() &
        validateRanks() &
        validateImages() &
        validateQuantity() &
        validateDefaultLevelsOverflow() &
        validateDefaultRanksOverflow()
}