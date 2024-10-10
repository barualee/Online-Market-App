$(document).ready(function() {
    $("#buttonCancel").on("click", function() {
        window.location= moduleURL;
    });

	$("#fileImage").change(function() {
		if(!checkFileSize(this)){
			return;
		}
		showImageThumbnail(this);
	});
});

function showImageThumbnail(fileInput){
	var file = fileInput.files[0];
	var reader = new FileReader();

	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function checkFileSize(fileInput){
    fileSize = fileInput.files[0].size;
    // alert("File size: "+fileSize);

    if(fileSize > MAX_FILE_SIZE){
        fileInput.setCustomValidity("Image size should be less than "+MAX_FILE_SIZE+" bytes!");
        fileInput.reportValidity();
        return false;
    } else {
        fileInput.setCustomValidity("");
        return true;
    }
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	
	// Show the modal using Bootstrap 5 API
	var modal = new bootstrap.Modal(document.getElementById('modalDialog'));
	modal.show();
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}