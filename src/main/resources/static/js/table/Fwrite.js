$(function() {
    $('#upload-btn').on('click', function() {
        $('#upfile').click();
    });

    $('#upfile').on('change', function() {
        var fileNames = $.map(this.files, function(file) {
            return file.name;
        }).join(', ');
        $('.upfileview').val(fileNames);
    });
});