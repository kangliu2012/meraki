    function edit(ele) {
        parentEle = ele.parentElement.parentElement
        url = "/organizations/edit/" + parentEle.children[1].innerHTML
        window.location = url
    }

    function del(ele) {
        var result = confirm("Do you really want to delete this organization?");
        if (result) {
              parentEle = ele.parentElement.parentElement
              url = "/organizations/delete/" + parentEle.children[1].innerHTML
              window.location = url
        }
    }