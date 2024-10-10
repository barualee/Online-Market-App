dropDownBrands = $("#brand");
dropDownCategories = $("#category");

$(document).ready(function() {
    
    $("#shortDescription").richText();
    $("#longDescription").richText();

    dropDownBrands.change(function() {
        dropDownCategories.empty();
        getCategories();
    });
    getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
    catIdField = $("#categoryId");
    editMode = false;

    if(catIdField.length){
        editMode = true;
    }
    if(!editMode) getCategories();
}

function getCategories() {
    brandId = dropDownBrands.val();
    url = brandModuleURL + "/" + brandId + "/categories";

    $.get(url, function(responseJson) {
        $.each(responseJson, function(index, category){
            // alert(category.name);
            $("<option>").val(category.id).text(category.name).appendTo(dropDownCategories);
        });
    });
}

function checkUnique(form){
    productId = $("#id").val();
    productName = $("#name").val();
    csrfValue = $("input[name='_csrf']").val();

    
    params = {id: productId, name: productName, _csrf: csrfValue};

    $.post(checkUniqueUrl, params, function(response) {
        //alert(response);
        if(response == "OK"){
            form.submit();
        } else if (response == "Duplicate"){
            showWarningModal("Product name already exist: "+ productName);
        } else {
            showErrorModal("Unkown response from server");
        }
    }).fail( function() {
        showErrorModal("Could not connect to server");
    });

    return false;
}