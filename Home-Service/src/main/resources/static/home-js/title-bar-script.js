const arrowUp = "▲";
const arrowDown = "▼";

let menuMap;

function activateMenu() {
    const menus = document.querySelectorAll('.title-bar-menu-wrapper');
    menus.forEach(menu => {

        menu.addEventListener("click", () => {
            const arrow = menu.querySelector('.title-menu-arrow');
            const menuId = menu.id;

            if (!menu.classList.contains('title-bar-focused')) {
                menus.forEach(m => {
                    m.classList.remove('title-bar-focused');
                    m.querySelector('.title-menu-arrow').textContent = arrowDown;
                });

                document.querySelectorAll('.dropdown-menu')
                    .forEach(dd => dd.classList.add('hidden'));

                menuMap[menuId].classList.remove('hidden');

                arrow.textContent = arrowUp;
                menu.classList.add('title-bar-focused');
            } else {
                menuMap[menuId].classList.add('hidden');

                arrow.textContent = arrowDown;
                menu.classList.remove('title-bar-focused');
            }
        });
    });
}

document.addEventListener("DOMContentLoaded", () => {
    menuMap = {
        "category-menu": document.getElementById('category-panel'),
        "filter-menu": document.getElementById('filter-panel'),
        "sort-menu": document.getElementById('sort-panel')
    };

    activateMenu();
});
