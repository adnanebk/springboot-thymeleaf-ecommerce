/**/
// domainUrl="https://adnane-shop.herokuapp.com/";
// baseurl="https://adnane-shop.herokuapp.com/productApi/";
let domainUrl="https://localhost:8080/";
let baseurl="https://localhost:8080/productApi/";

let token = document.getElementById('_csrf').getAttribute('content');
//var token = $("input[name='_csrf']").val();
let header = document.getElementById('_csrf_header').getAttribute('content');
$(document).ready(function(){

    $('input[type="file"]').change(function(e){
        var fileName = e.target.files[0].name;
        e.target.previousElementSibling.value=domainUrl+"uploadingDir/"+fileName;
        //e.target.previousElementSibling.disabled = true;
    });

});
let items;
let prodCount;
let prodLimit;
let page = 0;
let mybtn;


/*
    $("#editbtn").click(function () {
        alert("ccc");
    });*/
var prodId;


function cancelP() {
    //window.location.reload();
    $.ajax({
        url: baseurl+"cancelOperation",
        error:function (e) {
            console.log("ERROR: ");
            console.log(e);
        }
    })
}


// Prevent the form from submitting via the browser.



function SetId(k){

    $("#editItemModal").find(".errors").hide();
    $("#editItemModal").find("#item0").val(items[k].id);
    $("#editItemModal").find("#item1").val(items[k].name);
    $("#editItemModal").find("#item2").val(items[k].description);
    $("#editItemModal").find("#item3").val(items[k].price);
    $("#editItemModal").find("#item4").val(items[k].imageUrl);
    $("#editItemModal").find("#item5").val(items[k].imageUrl2);
    $("#editItemModal").find("#item6").val(items[k].imageUrl3);
    $("#editItemModal").find("#item7").val(items[k].imageUrl4);
    $("#editItemModal").find(".cats").val(items[k].categoryName);
    prodId= items[k].id;
}
function nextt() {
    page++;
    if((page*prodLimit)>=prodCount)
    {
        page= (parseInt(prodCount/prodLimit));
    }


    getall();
}
function prevv() {
    if(page<=0)
        page=0;
    else
    {
        page--;
        getall();
    }
}
function  getall() {

    var markup="";
    $.ajax({
        url: baseurl+"getAllProducts/page/"+page,
        error:function (e) {
            console.log("ERROR: ");
            console.log(e);
        }
    }).then(function(data) {
        items=data.productsDto;
        prodCount=data.prodCount;
        prodLimit=data.prodLimit;
        $('.cats').html(new Option("Choose one...",""));
        $('.cats').html(new Option("Choose one...",""));
        $.each(data.categories,function (k,v) {
            $('.cats').append(new Option(v,v));
            $('.cats').append(new Option(v,v));
        });


        $.each( data.productsDto, function( key, value ) {

            actionBtns="<td><a href=\"#editItemModal\" onclick='SetId("+key+")'   id='editbtn' class=\"edit\"   data-toggle=\"modal\"><i class=\"material-icons\" data-toggle=\"tooltip\" title=\"Edit\">&#xE254;</i></a> <a href=\"#deleteItemModal\" id='del' onclick='SetId("+key+")' test-id='"+value.id+"' class=\"delete\"  data-toggle=\"modal\"><i class=\"material-icons\" data-toggle=\"tooltip\" title=\"Delete\">&#xE872;</i></a></td>";

            markup = markup+"<tr>"+actionBtns+"<td>"+value.name+"</td><td>"+value.description.substr(0,40)+"...</td>" +
                "<td><a href='"+value.imageUrl+"'>"+value.imageUrl.substr(0,40)+"...</a></td>" +
                "<td><a href='"+value.imageUrl2+"'>"+value.imageUrl2.substr(0,40)+"...</a></td>" +
                " <td><a href='"+value.imageUrl3+"'>"+value.imageUrl3.substr(0,40)+"...</a></td>" +
                "<td><a href='"+value.imageUrl4+"'>"+value.imageUrl4.substr(0,40)+"...</a></td>" +
                "<td>"+value.price+"</td></tr>";

        });
        $("table tbody").html("");
        $("table tbody").append(markup);
        $("#paging").val((page+1));

    });
}
getall();
$(".myform").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    let form=$(this);
    let btn=form.find("input[type='submit']");
    btn.attr("disabled", true);
    form.find("div[class='addProg']").show();
    let type=btn.val()==="Add"?"POST":"PUT";
    ajaxPost(type,form);
    form.find("div[class='errors']").hide();
});
$("#myformcat").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    ajaxPostCat();
    $("#catErr0").hide();
});



//http://localhost:8080/api/create

function ajaxPostCat() {
    var productmodel = {
        name : $("#cat-name").val(),
        description : $("#cat-desc").val()
    };
    // DO POST
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url :baseurl+"category/create",
        data : JSON.stringify(productmodel),
        beforeSend: function( xhr ) {
            xhr.setRequestHeader(header, token);
        },
        success : function(result) {
            $('#addCatModal').modal('toggle');
            getall()
        },
        error : function(e) {
            $("#catErr0").show();
            console.log("ERROR: ", e);
        }
    });



}

function ajaxPost(type,form) {
    form.find("#err1").text("");
    form.find("#err2").text("");
    form.find("#err3").text("");
    form.find("#err4").text("");

    var isError=false;
    var formData = new FormData();
    var fileVal1 = form.find('#item4').val();
    var fileVal2 = form.find('#item5').val();
    var fileVal3 = form.find('#item6').val();
    var fileVal4 = form.find('#item7').val();

    let file1 = form.find('#fileInput1').get(0).files[0];
    let file2 = form.find('#fileInput2').get(0).files[0];
    let file3 = form.find('#fileInput3').get(0).files[0];
    let file4 = form.find('#fileInput4').get(0).files[0];

    if(fileVal1.length>0 && (fileVal1.toLowerCase().endsWith("jpg") || fileVal1.toLowerCase().endsWith("png") || fileVal1.toLowerCase().endsWith("jpeg") ))
        formData.append('files', file1);

    else if(fileVal1.length>0){
        $("#err1").text("This is not a valid image,you must use (Jpj,png) extensions");
        isError=true;
    }
    if( fileVal2.length>0 && (fileVal2.toLowerCase().endsWith("jpg") || fileVal2.toLowerCase().endsWith("png") || fileVal2.toLowerCase().endsWith("jpeg")))
        formData.append('files', file2);
    else if(fileVal2.length>0){
        $("#err2").text("This is not a valid image,you must use (Jpj,png) extensions");
        isError=true;
    }


    if(fileVal3.length>0 && (fileVal3.toLowerCase().endsWith("jpg") || fileVal3.toLowerCase().endsWith("png") || fileVal3.toLowerCase().endsWith("jpeg")))
        formData.append('files', file3);
    else if(fileVal3.length>0){
        $("#err3").text("This is not a valid image,you must use (Jpj,png) extensions");
        isError=true;
    }

    if(fileVal4.length>0 && (fileVal4.toLowerCase().endsWith("jpg") || fileVal4.toLowerCase().endsWith("png")  || fileVal4.toLowerCase().endsWith("jpeg")) )
        formData.append('files', file4);
    else if(fileVal4.length>0){
        $("#err4").text("This is not a valid image,you must use (Jpj,png) extensions");
        isError=true;
    }

    //formData.append('files',new Blob($("#fileInput2").file));

    // formData.append("prod",JSON.stringify(productmodel));
    // DO POST
    if(!isError)
    {

        $.ajax({
            processData: false, //prevent jQuery from automatically transforming the data into a query string
            type : "POST",
            enctype: 'multipart/form-data',
            url :baseurl+"createImgs",
            contentType: false,
            cache: false,
            data : formData,
            beforeSend: function( xhr ) {
                xhr.setRequestHeader(header, token);
            },
            success : function(result) {
                var productmodel = {
                    name : form.find("#item1").val(),
                    description : form.find("#item2").val(),
                    price : form.find("#item3").val(),
                    imageUrl : fileVal1,
                    imageUrl2 : fileVal2,
                    imageUrl3 : fileVal3,
                    imageUrl4 : fileVal4,
                    categoryName :$(".cats").find(":selected").val(),
                };
                $.ajax({
                    type : type,
                    url :baseurl+(type==="POST"?"create":"update"),
                    contentType: "application/json",
                    data : JSON.stringify(productmodel),
                    beforeSend: function( xhr ) {
                        xhr.setRequestHeader(header, token);
                    },
                    success:function(result){
                        form.find("#addbtn").attr("disabled",false);
                        form.find("div[class='addProg']").hide();
                        getall();
                        form.parent().parent().parent().modal('hide');
                        console.log(result);
                    },
                    error : function(e) {
                        form.find("#addbtn").attr("disabled",false);
                        form.find("div[class='addProg']").hide();
                        form.find("#AddError").text(e.responseJSON.message);
                        form.find("#AddError").show();
                        console.log("ERROR: ", e);
                    }
                })

            },
            error : function(e) {
                form.find("#addbtn").attr("disabled",false);
                form.find("div[class='addProg']").hide();
                $("#AddError").text(e.responseJSON.message);
                $("#AddError").show();
                console.log("ERROR: ", e);
            }
        });
    }


}









function DoDelet(){
    //var baseurl="http://adnanbk.herokuapp.com";

    $.ajax({
        type : "DELETE",
        //contentType : "application/json",
        url :baseurl+"delete/"+$("#item0").val(),
        //headers: {'X-XSRF-TOKEN': token},
        beforeSend: function( xhr ) {
            xhr.setRequestHeader(header, token);
        },
        success : function() {
            $('#deleteItemModal').modal('toggle');
            getall();

        },
        error : function(e) {
            $('#deleteItemModal').modal('toggle');
            mybtn.parents("tr").remove();
            console.log("ERROR: ", e);
        }
    });
}

$(document).ready(function(){
    // Activate tooltip
    $('[data-toggle="tooltip"]').tooltip();

    // Select/Deselect checkboxes
    var checkbox = $('table tbody input[type="checkbox"]');
    $("#selectAll").click(function(){
        if(this.checked){
            checkbox.each(function(){
                this.checked = true;
            });
        } else{
            checkbox.each(function(){
                this.checked = false;
            });
        }
    });
    checkbox.click(function(){
        if(!this.checked){
            $("#selectAll").prop("checked", false);
        }
    });
});

$("#addnewbtn").click(function () {
    $(".errors").hide();
    $(".errors").hide();
});

$("#paging").on('keyup', function (e) {
    if (e.keyCode == 13) {
        page=$("#paging").val()-1;
        getall();
    }
});

