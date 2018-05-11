var mditor;
var tale = new $.tale();
var attach_url = $('#attach_url').val();
// 每10分钟自动保存一次草稿
// var refreshIntervalId = setInterval("autoSave()", 10 * 60 * 1000);
Dropzone.autoDiscover = false;

$(document).ready(function () {

    mditor = window.mditor = Mditor.fromTextarea(document.getElementById('md-editor'));

    // Tags Input
    $('#tags').tagsInput({
        width: '100%',
        height: '35px',
        defaultText: '请输入文章标签'
    });

    $('.toggle').toggles({
        on: true,
        text: {
            on: '开启',
            off: '关闭'
        }
    });

    $("#multiple-sel").select2({
        width: '100%'
    });

    $('div.allow-false').toggles({
        off: true,
        text: {
            on: '开启',
            off: '关闭'
        }
    });

    if($('#thumb-toggle').attr('thumb_url') != ''){
        $('#thumb-toggle').toggles({
            off: true,
            text: {
                on: '开启',
                off: '关闭'
            }
        });
        $('#thumb-toggle').attr('on', 'true');
        $('#dropzone').css('background-image', 'url('+ $('#thumb-container').attr('thumb_url') +')');
        $('#dropzone').css('background-size', 'cover');
        $('#dropzone-container').show();
    } else {
        $('#thumb-toggle').toggles({
            off: true,
            text: {
                on: '开启',
                off: '关闭'
            }
        });
        $('#thumb-toggle').attr('on', 'false');
        $('#dropzone-container').hide();
    }

    var thumbdropzone = $('.dropzone');

    // 缩略图上传
    $("#dropzone").dropzone({
        url: "/admin/attach/upload",
        filesizeBase:1024,//定义字节算法 默认1000
        maxFilesize: '10', //MB
        fallback:function(){
            tale.alertError('暂不支持您的浏览器上传!');
        },
        acceptedFiles: 'image/*',
        dictFileTooBig:'您的文件超过10MB!',
        dictInvalidInputType:'不支持您上传的类型',
        init: function() {
            this.on('success', function (files, result) {
                console.log("upload success..");
                console.log(" result => " + result);
                if(result && result.success){
                    var url = attach_url + result.payload[0].fkey;
                    console.log('url => ' + url);
                    thumbdropzone.css('background-image', 'url('+ url +')');
                    thumbdropzone.css('background-size', 'cover');
                    $('.dz-image').hide();
                    $('#thumbimg').val(url);
                }
            });
            this.on('error', function (a, errorMessage, result) {
                if(!result.success && result.msg){
                    tale.alertError(result.msg || '缩略图上传失败');
                }
            });
        }
    });

});

/*
 * 自动保存为草稿
 * */
function  autoSave() {
    // var content = mditor.value;
    // var title = $('#articleForm input[name=title]').val();
    // if (title != '' && content != '') {
    //     $('#content-editor').val(content);
    //     $("#articleForm #categories").val($('#multiple-sel').val());
    //     var params = $("#articleForm").serialize();
    //     var url = $('#articleForm #cid').val() != '' ? '/admin/article/modify' : '/admin/article/publish';
    //     tale.post({
    //         url: url,
    //         data: params,
    //         success: function (result) {
    //             if (result && result.success) {
    //                 $('#articleForm #cid').val(result.payload);
    //             } else {
    //                 tale.alertError(result.msg || '保存文章失败');
    //             }
    //         }
    //     });
    // }
}
/**
 * 保存文章
 * @param status
 */
function subArticle(status) {
    var title = $('#articleForm input[name=title]').val();
    var content =  mditor.value;
    if (title == '') {
        tale.alertWarn('请输入文章标题');
        return;
    }
    if (content == '') {
        tale.alertWarn('请输入文章内容');
        return;
    }
    $('#content-editor').val(content);
    $("#articleForm #status").val(status);
    $("#articleForm #categories").val($('#multiple-sel').val());
    var params = $("#articleForm").serialize();
    var url = $('#articleForm #cid').val() != '' ? '/admin/article/modify' : '/admin/article/publish';
    tale.post({
        url:url,
        data:params,
        success: function (result) {
            if (result && result.success) {
                tale.alertOk({
                    text:'文章保存成功',
                    then: function () {
                        setTimeout(function () {
                            window.location.href = '/admin/article';
                        }, 500);
                    }
                });
            } else {
                tale.alertError(result.msg || '保存文章失败');
            }
        }
    });
}


function allow_comment(obj) {
    var this_ = $(obj);
    var on = this_.find('.toggle-on.active').length;
    var off = this_.find('.toggle-off.active').length;
    if (on == 1) {
        $('#allow_comment').val(false);
    }
    if (off == 1) {
        $('#allow_comment').val(true);
    }
}

function allow_ping(obj) {
    var this_ = $(obj);
    var on = this_.find('.toggle-on.active').length;
    var off = this_.find('.toggle-off.active').length;
    if (on == 1) {
        $('#allow_ping').val(false);
    }
    if (off == 1) {
        $('#allow_ping').val(true);
    }
}


function allow_feed(obj) {
    var this_ = $(obj);
    var on = this_.find('.toggle-on.active').length;
    var off = this_.find('.toggle-off.active').length;
    if (on == 1) {
        $('#allow_feed').val(false);
    }
    if (off == 1) {
        $('#allow_feed').val(true);
    }
}

function add_thumbimg(obj) {
    var this_ = $(obj);
    var on = this_.attr('on');
    console.log(on);
    if (on == 'true') {
        this_.attr('on', 'false');
        $('#dropzone-container').addClass('hide');
        $('#thumbImg').val('');
    } else {
        this_.attr('on', 'true');
        $('#dropzone-container').removeClass('hide');
        $('#dropzone-container').show();
    }
}