$(function() {
	$('.delete').on('click', function() {
		if (confirm("タスク内容："
		 + $(this).parents('.delete-button').find('input[name="content"]').val()
		 + "\n削除します。よろしいですか？")) {
			return true;
		}
		return false;
	});
});