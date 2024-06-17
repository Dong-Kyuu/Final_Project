$(function () {
    $('.department-name').on('click', function () {
        $(this).parent().toggleClass('show');
    });

    $('.department-checkbox').on('change', function () {
        var isChecked = $(this).is(':checked');
        $(this).parent().find('.employee-checkbox').each(function () {
            $(this).prop('checked', isChecked);
        });
    });

    $('#writeForm').submit(function(event) {
        const startDate = new Date($('#startDate').val());
        const endDateVal = $('#endDate').val();
        const title = $('#projectTitle').val().trim();

        let isValid = true;

        if (!title) {
            $('#titleError').show();
            isValid = false;
        } else {
            $('#titleError').hide();
        }

        if (endDateVal) {
            const endDate = new Date(endDateVal);
            if (startDate > endDate) {
                $('#dateError').show();
                isValid = false;
            } else {
                $('#dateError').hide();
            }
        }

        if (!isValid) {
            event.preventDefault();
        }

        // 체크된 직원만 전송하기 위해 체크되지 않은 항목 제거
        $('.employee-checkbox').each(function() {
            if (!$(this).is(':checked')) {
                $(this).closest('.employee').find('input').remove();
            }
        });
    });
    $('.department-checkbox').change(function() {
        const isChecked = $(this).is(':checked');
        $(this).closest('.department').find('.employee-checkbox').prop('checked', isChecked);
    });

    $('.employee-checkbox').change(function() {
        const departmentCheckbox = $(this).closest('.department').find('.department-checkbox');
        const allChecked = $(this).closest('.employee-list').find('.employee-checkbox').length === $(this).closest('.employee-list').find('.employee-checkbox:checked').length;
        departmentCheckbox.prop('checked', allChecked);
    });
});