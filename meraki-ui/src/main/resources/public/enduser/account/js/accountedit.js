    function edit(ele) {
        parentEle = ele.parentElement.parentElement
        url = "/accounts/edit/" + parentEle.children[0].innerHTML
        window.location = url
    }

    function del(ele) {
         var result = confirm("Do you really want to delete this account?");
         if (result) {
              parentEle = ele.parentElement.parentElement
              url = "/accounts/delete/" + parentEle.children[0].innerHTML
              window.location = url
         }
    }
