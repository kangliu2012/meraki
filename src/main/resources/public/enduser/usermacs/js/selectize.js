
function initializeSelectize(contactList) {
    var selectrows = document.getElementsByName("select-links");
    ct = 0
    while(1) {
        if( ct == selectrows.length) break
        var options = JSON.parse(JSON.stringify(contactList[ct].clientUIList))
        var $select = $(selectrows[ct]).selectize({
	        valueField: 'id',
	        labelField: 'mac',
	        searchField: 'mac',
	        options: options,
	        maxItems: 5,
	        persist: true,
	        loadThrottle: 600,
	        create: false,
	        allowEmptyOption: false,
	        onItemAdd: function (value, $item) {
	            if( ct == contactList.length) {
                       var str = String(this.eventNS)
                       idx = parseInt(str.substring(10),10)
                       data = {}
                       data['clientId'] = String(value)
                       data['contactId'] = contactList[idx - 1].id
                       $.ajax({
   		                contentType : "application/json",
   		                url: '/addClientToContact',
   		                type: 'POST',
   		                dataType: 'json',
   		                async: true,
   		                data: JSON.stringify(data),
   		                error: function() {
   		                },
   		                success: function(res) {
                               <!--var trRows = document.getElementsByName("trContact");-->
                               <!--record = trRows[idx - 1]-->
                               <!--var saveSpan = record.getElementsByTagName("span")[0];-->
                               <!--saveSpan.style.visibility = "visible"-->
                               <!--$(saveSpan).fadeOut(1000, function() {-->
                               <!--saveSpan.style= "inline"-->
                               <!--saveSpan.style.visibility = "hidden"-->
                               <!--})-->
   		                }
                       });
	            }
               },
               onItemRemove: function(value, $item) {
                       var str = String(this.eventNS)
                       idx = parseInt(str.substring(10),10)
                       data = {}
                       data['clientId'] = String(value)
                       data['contactId'] = contactList[idx - 1].id
                       $.ajax({
   		                contentType : "application/json",
   		                url: '/removeClientFromContact',
   		                type: 'POST',
   		                dataType: 'json',
   		                async: true,
   		                data: JSON.stringify(data),
   		                error: function() {

   		                },
   		                success: function(res) {
//                               alert("deleted " + String($item[0].innerHTML) + " Successfully!")
   		                }
                       });
               },
	        load: function(query, callback) {
                   var str = String(this.eventNS)
                   idx = parseInt(str.substring(10),10)
		        this.renderCache = {};
		        <!--self = this-->
		        <!--$.each(self.options, function(key, value) {-->
			    <!--if(self.items.indexOf(key) == -1) {-->
				<!--delete self.options[key];-->
			    <!--}-->
		        <!--});-->
		        <!--self.sifter.items = self.options;-->
   		        if ( !query.length ) return callback();
   		        var arr = [1, 2, 3, 4, 5, 6, 9, 12, 15]
   		        if ( arr.indexOf(query.length) != -1 ) return callback();
   		        data = {}
                   data['mac'] = query
                   data['contactId'] = contactList[idx - 1].id

   	            $.ajax({
   		            contentType : "application/json",
   		            url: '/searchMacByOrgID',
   		            type: 'POST',
   		            dataType: 'json',
   		            async: false,
   		            data: JSON.stringify(data),
   		            error: function() {
                           callback();
   		            },
   		            success: function(res) {
   			            clientUIList = res["clientList"]
			            var datalist = JSON.parse(JSON.stringify(clientUIList));
			            callback(datalist)
   		            }
                   });
               }

    });

    selectize = $select[0].selectize
       var options = JSON.parse(JSON.stringify(contactList[ct].clientUIList))
       var arrVal = []
       if( options != null ) {
           idx = 0
           while(1) {
               if( idx == options.length) break
               arrVal.push(options[idx].id)
               idx++
           }
           selectize.setValue(arrVal)
       }
       ct++
   }
}
