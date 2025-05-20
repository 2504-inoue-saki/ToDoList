$(function() {
	$('.update-states-button').on('click', function() {
		if (confirm("ステータスを変更します。よろしいですか？")) {
			return true;
		}
		return false;
	});
});