$("#fileInput").change(function(){
    console.log($(this).val());
    console.log(this);
    if (this.files && this.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#imagePreview').attr('src', e.target.result);
        }
        reader.readAsDataURL(this.files[0]);
    }
})

$("#deleteButton").click(function(){
    if (confirm("Are you sure you want to delete this item?") == true){
        //submit delete form
        $("#deleteMenuItemForm").submit();
    }
})