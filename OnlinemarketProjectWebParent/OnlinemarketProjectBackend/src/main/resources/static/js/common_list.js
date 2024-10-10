function clearFilter(){
    window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName){
    entityId = link.attr("entityId");

    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Are you sure to delete this "+entityName+" ID "+entityId+" ?");
    
    var modal = new bootstrap.Modal(document.getElementById('confirmModal'));
    modal.show();
}