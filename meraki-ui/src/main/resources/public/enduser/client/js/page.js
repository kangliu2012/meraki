
function initializePage(locid, curPage, totalPages) {
    url = window.location.pathname.substring(1)
    url = url.substring(0, url.indexOf('/'))
    url += ("/" + locid)
    var ele = document.getElementById("page")
    iCurPage = parseInt(curPage)
    iTotalPages = parseInt(totalPages)
    pre = "Page"
    if( iCurPage > 1 ) {
        pre += "<a rel='prev' href='/" + url + "/page/" + String(iCurPage - 1) + "'" + " > << </a>"
    } else {
//        pre = ("<span class='disabled'>&#8592; Previous</span>")
    }
    ele.innerHTML = pre
    st = 1
    if( iCurPage > 5 ) {
        st = iCurPage - 2
    }
    ed = st + 4
    if( ed > iTotalPages) {
        ed = iTotalPages
    }
    while(1) {
        if( st > ed ) break
        if( st == iCurPage ) {
            ele.innerHTML += ("<scan> " + iCurPage + "</scan>")
        } else {
            if( st <= iTotalPages ) {
                ele.innerHTML += ("<a href='/" + url + "/page/" + st + "'"  +  "> " + st + "</a>")
            } else {
                ele.innerHTML += ("<scan> " + st + "</scan>")
            }
        }
        st++
    }
    if( iTotalPages <= ed ) {
//        ele.innerHTML += ("<span class='disabled'> Next &#8594;</span>")
    } else {
        ele.innerHTML += ("<a rel='next' href='/" + url + "/page/" + String(iCurPage + 1) + "'" + "> >> </a>")
    }
}
