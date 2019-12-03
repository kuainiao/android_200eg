
function clearFileList() {
	for (; g_file_list.length; ) {
		g_file_list.pop();
	}
}
function isFileExist(a) {
	var b, c = "false";
	for (b in g_file_list) {
		if (g_file_list[b] == a) {
			c = "true";
			break;
		}
	}
	return c;
}
function addFile(a) {
	g_file_list.push(a);
}
function disp_rename(a) {
	var c, d, e, b = Base64.decode(a);
	return null != b && (c = b.lastIndexOf("."), c > 0 && (d = b.substring(c + 1), "ndl" == d)) ? (alert("\xd4\xda\xcf\xdf\xca\xe9\xbc\xae\xbc\xc7\xc2\xbc\xb2\xbb\xd4\xca\xd0\xed\xd6\xd8\xc3\xfc\xc3\xfb!"), void 0) : (e = prompt("\xc7\xeb\xca\xe4\xc8\xeb\xd0\xc2\xb5\xc4\xce\xc4\xbc\xfe\xc3\xfb\xa3\xba", Base64.decode(a)), null != e && "" != e && e != Base64.decode(a) && ("true" == IsContainInvalidChars(e) ? alert("\xce\xc4\xbc\xfe\xc3\xfb\xb0\xfc\xba\xac\xa1\xb0/\\&<>*:\"\xbf\xd5\xb8\xf1\xa1\xb1\xb5\xc8\xb7\xc7\xb7\xa8\xd7\xd6\xb7\xfb\xbb\xf2\xd5\xdf\xce\xc4\xbc\xfe\xc3\xfb\xce\xaa\xa1\xb0.\xa1\xb1\xa3\xac\xc7\xeb\xd6\xd8\xd0\xc2\xca\xe4\xc8\xeb") : "false" == isFileExist(e) ? ($("#idoperatortype").val("3"), $("#idnewnameInput").val(Base64.encode(e)), $("#idoperatorfilename").val(a), $("#form1").submit()) : alert("\xd0\xc2\xb5\xc4\xce\xc4\xbc\xfe\xc3\xfb\"" + e + "\"\xd2\xd1\xbe\xb4\xe6\xd4\xda\xa3\xac\xc7\xeb\xbb\xbb\xb8\xf6\xce\xc4\xbc\xfe\xc3\xfb")), void 0);
}
function transfer_filename(a) {
	var b;
	return b = a.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/ /g, "&nbsp;").replace(/"/g, "&quot;");
}
function IsContainInvalidChars(a) {
	return null != a.match(/[\/\\\&\ \<\>\*\:\"]/) ? "true" : null == a.match(/[^.]/) ? "true" : "false";
}
function disp_dirCreate() {
	var a = document.getElementById("dirInputname").value;
	null != a && "" != a ? "false" == isFileExist(a) ? "true" == IsContainInvalidChars(a) ? alert("\xce\xc4\xbc\xfe\xc3\xfb\xb0\xfc\xba\xac\xa1\xb0/\\&<>*:\"\xbf\xd5\xb8\xf1\xa1\xb1\xb5\xc8\xb7\xc7\xb7\xa8\xd7\xd6\xb7\xfb\xbb\xf2\xd5\xdf\xce\xc4\xbc\xfe\xc3\xfb\xce\xaa\xa1\xb0.\xa1\xb1\xa3\xac\xc7\xeb\xd6\xd8\xd0\xc2\xca\xe4\xc8\xeb") : ($("#idoperatorfilename").val(Base64.encode(a)), $("#idoperatortype").val("2"), $("#form1").submit()) : alert("\xce\xc4\xbc\xfe\xbb\xf2\xc4\xbf\xc2\xbc\"" + a + "\"\xd2\xd1\xbe\xb4\xe6\xd4\xda\xa3\xac\xc7\xeb\xbb\xbb\xb8\xf6\xce\xc4\xbc\xfe\xc3\xfb") : alert("\xc7\xeb\xca\xe4\xc8\xeb\xd2\xaa\xb4\xb4\xbd\xa8\xb5\xc4\xc4\xbf\xc2\xbc\xc3\xfb\xb3\xc6");
}
function disp_delete(a, b) {
	var c, d;
	"true" == b ? (c = confirm("\xc8\xb7\xc8\xcf\xd2\xaa\xc9\xbe\xb3\xfd\xc4\xbf\xc2\xbc\"" + Base64.decode(a) + "\"\xbc\xb0\xc4\xbf\xc2\xbc\xcf\xc2\xb5\xc4\xcb\xf9\xd3\xd0\xc4\xda\xc8\xdd\xc2\xf0"), 1 == c && ($("#idoperatortype").val("1"), $("#idoperatorfilename").val(a), $("#form1").submit())) : (c = confirm("\xc8\xb7\xc8\xcf\xd2\xaa\xc9\xbe\xb3\xfd\xce\xc4\xbc\xfe\"" + Base64.decode(a) + "\"\xc2\xf0?"), 1 == c && ($("#idoperatortype").val("1"), $("#idoperatorfilename").val(a), d = {act:$("#idoperatortype").val(), filename:$("#idoperatorfilename").val(), temprenamedata:""}, $.ajax({type:"post", url:window.location.href, data:d, dataType:"jsonp", jsonp:"callback", complete:function () {
		loadDir();
	}})));
}
function uploadfile() {
	var b, c, a = document.getElementById("file_upload").value;
	null != a && "" != a && (b = a, c = a.lastIndexOf("\\"), -1 != c && (b = a.substring(c + 1)), "false" == isFileExist(b) ? $("#form2").submit() : alert("\xce\xc4\xbc\xfe\"" + b + "\"\xd2\xd1\xbe\xb4\xe6\xd4\xda\xa3\xac\xc7\xeb\xbb\xbb\xb8\xf6\xce\xc4\xbc\xfe\xc3\xfb\xd4\xd9\xc9\xcf\xb4\xab"));
}
function showtable() {
	var g, a = "ndtable", b = "#FCF9D8", c = "#EEEEEE", d = "#FFFFFF", e = document.getElementById(a), f = e.getElementsByTagName("tr");
	for (g = 1; g < f.length; g++) {
		f[g].onmouseover = function () {
			this.style.backgroundColor = b;
		}, f[g].onmouseout = function () {
			this.style.backgroundColor = 0 == this.rowIndex % 2 ? c : d;
		}, f[g].className = 0 == g % 2 ? "color1" : "color2";
	}
}
function onResponse(a) {
	txt = "<div class=\"title\"><h1>\xce\xc4\xbc\xfe\xcd\xac\xb2\xbd\xb4\xab\xca\xe4</h1></div>", txt += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"3\" id=\"ndtable\">", txt += "<tr class=\"tr1bg\"> <td>&nbsp;</td><td>\xc3\xfb\xb3\xc6</td> <td>\xd0\xde\xb8\xc4\xca\xb1\xbc\xe4</td><td>\xb4\xf3\xd0\xa1</td><td class=\"td4wd\">&nbsp;</td></tr>";
	var b = window.location.href + "..";
	txt = txt + "<tr ><td>&nbsp;</td><td><a href=\"" + b + "\">\xc9\xcf\xd2\xbb\xbc\xb6</a></td><td>--</td><td>--</td><td></td><td></td></tr>", clearFileList(), addFile("tmp"), $(a).find("fileinfo").each(function (a) {
		var g, i, j, k, b = null, c = null, d = null, e = null, f = "false";
		b = 0 == a % 2 ? "\"#EEEEEE\"" : "\"#FFFFFF\"", txt += "<tr >", txt += "<td>&nbsp;</td>", f = $(this).children("isdir").text(), d = $(this).children("nameencode").text(), e = $(this).children("uri").text(), c = $(this).children("name").text(), g = Base64.decode(c), addFile(g), atxt = "true" == f ? "<a href=\"" + e + "\">" + "<img src=\"/tmp/images/dir.gif\" width=\"20\" height=\"20\" alt=\"dir\" />" + transfer_filename(g) + "/</a>" : "<a href=\"" + e + "\" target='_blank'>" + "<img src=\"/tmp/images/file.gif\" width=\"20\" height=\"20\" alt=\"File\" />" + transfer_filename(g) + "</a>", txt = txt + "<td>" + atxt + "</td>", xx = $(this).children("time").text();
		try {
			txt = txt + "<td>" + xx + "</td>";
		}
		catch (h) {
			txt += "<td> </td>";
		}
		xx = $(this).children("size").text();
		try {
			"true" == f ? txt = txt + "<td>" + "--" + "</td>" : (i = null, i = 1024 > xx ? Number(xx).toFixed(0) : 1048576 > xx ? Number(xx / 1024).toFixed(1) + "k" : 1073741824 > xx ? Number(xx / 1048576).toFixed(2) + "M" : Number(xx / 1073741824).toFixed(2) + "G", txt = txt + "<td>" + i + "</td>");
		}
		catch (h) {
			txt += "<td> </td>";
		}
		txt = txt + "<td class=\"td4wd\"><input type='button' style='background-image:url(/tmp/images/del.gif); background-color: Transparent; border-style:none; width:73px; height:25px;' name='" + c + "' id='" + f + "' width=83px; height=25px; onclick='disp_delete(this.name, this.id)' />", j = !1, null != g && (a = g.lastIndexOf("."), a > 0 && (k = g.substring(a + 1), "ndl" == k && (j = !0))), txt = j ? txt + "<input type='button' disabled style='background-image:url(/tmp/images/rename_disable.gif); background-color: Transparent; border-style:none; width:83px; height:25px;' name='" + c + "' onclick='disp_rename(this.name)' /></td>" : txt + "<input type='button' style='background-image:url(/tmp/images/rename.gif); background-color: Transparent; border-style:none; width:83px; height:25px;' name='" + c + "' onclick='disp_rename(this.name)' /></td>", txt += "</tr>";
	}), txt += "</table>", txt = txt + "<form  method=\"POST\" id=\"form1\">" + "<input type=\"hidden\" id=\"idoperatortype\" name=\"act\">", txt += "<input type=\"hidden\" id=\"idoperatorfilename\" name=\"filename\">", txt += " <input type=\"hidden\" id=\"idnewnameInput\" name=\"temprenamedata\"></form>", txt = txt + " <table><tr><td><label class=\"labels\" for=\"file\">&nbsp;&nbsp;\xca\xe4\xb3\xf6\xd2\xaa\xb4\xb4\xbd\xa8\xc4\xbf\xc2\xbc\xb5\xc4\xc3\xfb\xb3\xc6</label>" + "<input type=\"text\" id=\"dirInputname\"></td>" + "<td><input type=\"button\" style='background-image:url(/tmp/images/newdir.gif);background-color: Transparent; border-style:none; width:83px; height:25px;' onclick='disp_dirCreate()'></td></tr></table>", document.getElementById("nddiv").innerHTML = txt, showtable();
}
function showBg() {
	var a = Math.max(document.body.scrollHeight, document.body.clientHeight), b = $("body").width();
	$("#fullbg").css({height:a, width:b, display:"block"}), $("#dialog").show();
}
function closeBg(a) {
	$("#fullbg,#dialog").hide(), a && alert("\xcd\xf8\xc2\xe7\xb4\xab\xca\xe4\xb4\xed\xce\xf3");
}
function loadDir() {
			new SWFUpload({
				// Backend Settings
				upload_url: "http://192.168.3.187:8089/jquery1/servlet/UpLoadServlet",
				post_params: "",

				// File Upload Settings
				file_size_limit : "100 MB",
				file_types : "*.txt",
				file_types_description : "*",
				file_upload_limit : 0,
				
				//swfupload_preload_handler : preLoad,
				//swfupload_load_failed_handler : loadFailed,
				//file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				//upload_progress_handler : uploadProgress,
				//upload_error_handler : uploadError,
				//upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,
				
				 file_queued_handler : fileQueued,
				 upload_start_handler : uploadStart,
				 upload_progress_handler : uploadProgress,
				 upload_success_handler : uploadSuccess,
				

				// Button Settings
				button_image_url : "",
				button_placeholder_id : "spanButtonPlaceholder",
				button_width: 230,
				button_height: 60,
				button_text : 'ccc',
				button_text_style : '',
				button_text_top_padding: 0,
				button_text_left_padding: 18,
				button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
				button_cursor: SWFUpload.CURSOR.HAND,
				
				// Flash Settings
				flash_url : "/.wfs/img/swfupload.swf",
				flash9_url : "/.wfs/img/swfupload_fp9.swf",

				custom_settings : {
					upload_target : "divFileProgressContainer",
					thumbnail_height: 400,
					thumbnail_width: 400,
					thumbnail_quality: 100
				},
				
				// Debug Settings   true debug
				debug: false
			});
}
function queueComplete() {
	closeBg(!1), loadDir(), this.setButtonDisabled(!1);
}
function fileDialogComplete(a, b) {
	try {
		b > 0 && (showBg(), this.startUpload(), this.setButtonDisabled(!0));
	}
	catch (c) {
		this.debug(c);
	}
}
function fileQueueError(a, b, c) {
	try {
		if (b === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			return alert("You have attempted to queue too many files.\n" + (0 === c ? "You have reached the upload limit." : "You may select " + (c > 1 ? "up to " + c + " files." : "one file."))), void 0;
		}
		var d = new FileProgress(a, this.customSettings.progressTarget);
		switch (d.setError(), d.toggleCancel(!1), b) {
		  case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			d.setStatus("File is too big."), this.debug("Error Code: File too big, File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
			break;
		  case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			d.setStatus("Cannot upload Zero Byte files."), this.debug("Error Code: Zero byte file, File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
			break;
		  case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			d.setStatus("Invalid File Type."), this.debug("Error Code: Invalid File Type, File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
			break;
		  default:
			null !== a && d.setStatus("Unhandled Error"), this.debug("Error Code: " + b + ", File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
		}
	}
	catch (e) {
		this.debug(e);
	}
}
function uploadError(a, b, c) {
	try {
		this.cancelQueue(), b == SWFUpload.UPLOAD_ERROR.IO_ERROR ? closeBg(!0) : closeBg(!1);
		var d = new FileProgress(a, this.customSettings.progressTarget);
		switch (d.setError(), d.toggleCancel(!1), b) {
		  case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			d.setStatus("Upload Error: " + c), this.debug("Error Code: HTTP Error, File name: " + a.name + ", Message: " + c);
			break;
		  case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			d.setStatus("Upload Failed."), this.debug("Error Code: Upload Failed, File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
			break;
		  case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			d.setStatus("Server (IO) Error"), this.debug("Error Code: IO Error, File name: " + a.name + ", Message: " + c);
			break;
		  case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			d.setStatus("Security Error"), this.debug("Error Code: Security Error, File name: " + a.name + ", Message: " + c);
			break;
		  case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			d.setStatus("Upload limit exceeded."), this.debug("Error Code: Upload Limit Exceeded, File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
			break;
		  case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			d.setStatus("Failed Validation.  Upload skipped."), this.debug("Error Code: File Validation Failed, File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
			break;
		  case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			0 === this.getStats().files_queued, d.setStatus("Cancelled"), d.setCancelled();
			break;
		  case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			d.setStatus("Stopped");
			break;
		  default:
			d.setStatus("Unhandled Error: " + b), this.debug("Error Code: " + b + ", File name: " + a.name + ", File size: " + a.size + ", Message: " + c);
		}
	}
	catch (e) {
		this.debug(e);
	}
}
var Base64 = {_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=", encode:function (a) {
	var c, d, e, f, g, h, i, b = "", j = 0;
	for (a = Base64._utf8_encode(a); j < a.length; ) {
		c = a.charCodeAt(j++), d = a.charCodeAt(j++), e = a.charCodeAt(j++), f = c >> 2, g = (3 & c) << 4 | d >> 4, h = (15 & d) << 2 | e >> 6, i = 63 & e, isNaN(d) ? h = i = 64 : isNaN(e) && (i = 64), b = b + this._keyStr.charAt(f) + this._keyStr.charAt(g) + this._keyStr.charAt(h) + this._keyStr.charAt(i);
	}
	return b;
}, decode:function (a) {
	var c, d, e, f, g, h, i, b = "", j = 0;
	for (a = a.replace(/[^A-Za-z0-9\+\/\=]/g, ""); j < a.length; ) {
		f = this._keyStr.indexOf(a.charAt(j++)), g = this._keyStr.indexOf(a.charAt(j++)), h = this._keyStr.indexOf(a.charAt(j++)), i = this._keyStr.indexOf(a.charAt(j++)), c = f << 2 | g >> 4, d = (15 & g) << 4 | h >> 2, e = (3 & h) << 6 | i, b += String.fromCharCode(c), 64 != h && (b += String.fromCharCode(d)), 64 != i && (b += String.fromCharCode(e));
	}
	return b = Base64._utf8_decode(b);
}, _utf8_encode:function (a) {
	var b, c, d;
	for (a = a.replace(/\r\n/g, "\n"), b = "", c = 0; c < a.length; c++) {
		d = a.charCodeAt(c), 128 > d ? b += String.fromCharCode(d) : d > 127 && 2048 > d ? (b += String.fromCharCode(192 | d >> 6), b += String.fromCharCode(128 | 63 & d)) : (b += String.fromCharCode(224 | d >> 12), b += String.fromCharCode(128 | 63 & d >> 6), b += String.fromCharCode(128 | 63 & d));
	}
	return b;
}, _utf8_decode:function (a) {
	for (var b = "", c = 0, d = c1 = c2 = 0; c < a.length; ) {
		d = a.charCodeAt(c), 128 > d ? (b += String.fromCharCode(d), c++) : d > 191 && 224 > d ? (c2 = a.charCodeAt(c + 1), b += String.fromCharCode((31 & d) << 6 | 63 & c2), c += 2) : (c2 = a.charCodeAt(c + 1), c3 = a.charCodeAt(c + 2), b += String.fromCharCode((15 & d) << 12 | (63 & c2) << 6 | 63 & c3), c += 3);
	}
	return b;
}}, g_file_list = new Array;

