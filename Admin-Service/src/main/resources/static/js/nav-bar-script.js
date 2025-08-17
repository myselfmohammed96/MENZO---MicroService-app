const searchIcon = document.getElementById("search-icon-img");
const searchBar = document.getElementById("search-bar");
const clearSearch = document.getElementById("clear-search");
const searchBtn = document.getElementById("search-lens-btn");

searchIcon.onclick = async function (event) {
    event.stopPropagation();
    openSearchBar();
}

clearSearch.onclick = async function (event) {
    event.stopPropagation();
    closeSearchBar();
}

searchBtn.onclick = async function (event) {
    event.stopPropagation();
    const searchText = searchBar.value.trim();
    searchInput(searchText);
}

function openSearchBar() {
    searchBar.style.display = "block";
    searchBar.focus();
    clearSearch.style.display = "block";
    searchBtn.style.display = "block";
    searchIcon.style.display = "none";
}

function closeSearchBar() {
    searchBar.value = null;
    searchBar.style.display = "none";
    clearSearch.style.display = "none";
    searchBtn.style.display = "none";
    searchIcon.style.display = "block";
}

function searchInput(searchText) {
    // const searchText = searchBar.value.trim();
    console.log("search Ops incomplete. You have entered ", searchText);
    closeSearchBar();
}