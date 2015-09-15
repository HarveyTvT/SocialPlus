/* 
 * @Author: gyl
 * @Date:   2015-09-01 09:40:24
 * @Last Modified by:   gyl
 * @Last Modified time: 2015-09-15 11:01:36
 */
$(document).ready(function() {

    $('#submit_box').hide();
    $('#chooselogin_btn').click(function() {
        $('#submit_box').hide();
        $('#login_box').show();
    });

    $('#choosesubmit_btn').click(function() {
        $('#login_box').hide();
        $('#submit_box').show();
    });

    $('.ui.form')
        .form({
            fields: {
                email: {
                    identifier: 'email',
                    rules: [{
                        type: 'empty',
                        prompt: '请输入邮箱地址'
                    }, {
                        type: 'email',
                        prompt: '请输入有效的邮箱地址'
                    }]
                },
                password: {
                    identifier: 'password',
                    rules: [{
                        type: 'empty',
                        prompt: '请输入密码'
                    }, {
                        type: 'length[6]',
                        prompt: '密码必须长度大于6位'
                    }]
                }

            },
            inline: true,
            on: 'blur'
        });
    $('#submit_box form')
        .form({
            fields: { 
                match: {
                    identifier: 'confirmpswd',
                    rules: [{
                        type: 'match[password]',
                        prompt: '两次密码不一致'
                    }]
                }
            },
            inline: true,
            on: 'blur'
        });
});