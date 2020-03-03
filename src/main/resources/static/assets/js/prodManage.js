
//let domainUrl="https://adnane-shop.herokuapp.com/";
//let baseurl="https://adnane-shop.herokuapp.com/productApi/";
let domainUrl="https://localhost:8080/";
let baseurl="https://localhost:8080/productApi/";

//let token = document.getElementById('_csrf').getAttribute('content');
//let token = $("input[name='_csrf']").val();
//let header = document.getElementById('_csrf_header').getAttribute('content');
$(document).ready(function(){

    $('input[type="file"]').change(function(e){
        let fileName = e.target.files[0].name;
        e.target.previousElementSibling.value=fileName;
        //e.target.previousElementSibling.disabled = true;
    });

});
let items;
let prodCount;
let prodLimit;
let _page = 0;
let mybtn;


/*
    $("#editbtn").click(function () {
        alert("ccc");
    });*/
let prodId;


function cancelP() {
    //window.location.reload();
/*    $.ajax({
        url: "/productApi/cancelOperation",
        error:function (e) {
            console.log("ERROR: ");
            console.log(e);
        }
    })*/
let form=$("#myform");
let form2=$("#myform2");
    form.find("div[class='addProg']").hide();
    form.find("input[type='submit']").show();
    form2.find("div[class='addProg']").hide();
    form2.find("input[type='submit']").show();
}


// Prevent the form from submitting via the browser.



function SetId(k){
    $("#err11").text("");
    $("#err12").text("");
    $("#err13").text("");
    $("#err14").text("");
    $("#EditError").hide();
    $("#AddError").hide();
    $("#item0").val(items[k].id);
    $("#item1").val(items[k].name);
    $("#item2").val(items[k].description);
    $("#item3").val(items[k].price);
    $("#item4").val(items[k].imageUrl);
    $("#item5").val(items[k].imageUrl2);
    $("#item6").val(items[k].imageUrl3);
    $("#item7").val(items[k].imageUrl4);
    $(".cats").val(items[k].categoryName);
    prodId= items[k].id;
}
function nextt() {
    _page++;
    if((_page*prodLimit)>=prodCount)
    {
        _page= (parseInt(prodCount/prodLimit));
    }


    getall();
}
function prevv() {
    if(_page<=0)
        _page=0;
    else
    {
        _page--;
        getall();
    }
}
function  getall() {

    let markup="";
    $.ajax({
        url: "/productApi/products?page="+_page,
        error:function (e) {
            console.log("ERROR: ");
            console.log(e);
        }
    }).then(function(data) {
        items=data.productsDto;
        prodCount=data.prodCount;
        prodLimit=data.prodLimit;
        $('.add-cats').html(new Option("Choose one...",""));
        $('.cats').html(new Option("Choose one...",""));
        $.each(data.categories,function (k,v) {
            $('.add-cats').append(new Option(v,v));
            $('.cats').append(new Option(v,v));
        });


        $.each( data.productsDto, function( key, value ) {


            actionBtns="<td><a href=\"#editItemModal\" onclick='SetId("+key+")'   id='editbtn' class=\"edit\"   data-toggle=\"modal\"><i class=\"material-icons\" data-toggle=\"tooltip\" title=\"Edit\">&#xE254;</i></a> <a href=\"#deleteItemModal\" id='del' onclick='SetId("+key+")' test-id='"+value.id+"' class=\"delete\"  data-toggle=\"modal\"><i class=\"material-icons\" data-toggle=\"tooltip\" title=\"Delete\">&#xE872;</i></a></td>";

            let i1=value.imageUrl===""?"#":value.imageUrl;
            let i2=value.imageUrl2===""?"#":value.imageUrl2;
            let i3=value.imageUrl3===""?"#":value.imageUrl3;
            let i4=value.imageUrl4===""?"#":value.imageUrl4;

            markup = markup+"<tr>"+actionBtns+"<td>"+value.name+"</td><td>"+value.description.substr(0,40)+"...</td>" +
                "<td><a href='"+i1+"'>Url</a></td>" +
                "<td><a href='"+i2+"'>Url</a></td>" +
                " <td><a href='"+i3+"'>Url</a></td>" +
                "<td><a href='"+i4+"'>Url</a></td>" +
                "<td>"+value.price+"</td></tr>";

        });
        $("table tbody").html("");
        $("table tbody").append(markup);
        $("#paging").val((_page+1));

    });
}

getall();

$("#myform").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    ajaxPost($("#myform"));
    $("#AddError").hide();
});
$("#myformcat").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    ajaxPostCat();
    $("#catErr0").hide();
});
$("#myform2").submit(function (event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    ajaxPut($("#myform2"));
});


$("#editbtn").click(function () {
});


//http://localhost:8080/api/create

function ajaxPostCat() {
    let productmodel = {
        name : $("#cat-name").val(),
        description : $("#cat-desc").val()
    };
    // DO POST
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url :"/productApi/category/create",
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

function ajaxPost(form) {
    let err1=$("#err1");
    let err2=$("#err2");
    let err3=$("#err3");
    let err4=$("#err4");
    err1.text("");
    err2.text("");
    err3.text("");
    err4.text("");
    let isError=false;
    let formData = new FormData();
    let fileVal1 = document.getElementById('add-item4').value;
    let fileVal2 = document.getElementById('add-item5').value;
    let fileVal3 = document.getElementById('add-item6').value;
    let fileVal4 = document.getElementById('add-item7').value;

    let file1 = document.getElementById('fileInput1').files[0];
    let file2 = document.getElementById('fileInput2').files[0];
    let file3 = document.getElementById('fileInput3').files[0];
    let file4 = document.getElementById('fileInput4').files[0];


    isError = validateImage(fileVal1,fileVal2,fileVal3,fileVal4,file1,file2,file3 ,file4, err1, err2, err3, err4,formData,isError);



    //formData.append('files',new Blob($("#fileInput2").file));

    // formData.append("prod",JSON.stringify(productmodel));
    // DO POST
    if(!isError)
    {
        let productmodel = {
            name : $("#add-item1").val(),
            description : $("#add-item2").val(),
            price : $("#add-item3").val(),
            imageUrl : fileVal1,
            imageUrl2 : fileVal2,
            imageUrl3 : fileVal3,
            imageUrl4 : fileVal4,
            categoryName :$(".add-cats").find(":selected").val(),
        };

        let displayError=$("#AddError");
        form.find("input[type='submit']").hide();
        form.find("div[class='addProg']").show();
        updateServer(form,formData, productmodel, displayError,"POST");
    }


}

function ajaxPut(form) {

    let isError=false;
    let formData = new FormData();
    let fileVal1 = document.getElementById('item4').value;
    let fileVal2 = document.getElementById('item5').value;
    let fileVal3 = document.getElementById('item6').value;
    let fileVal4 = document.getElementById('item7').value;

    let file1 = document.getElementById('fileInput11').files[0];
    let file2 = document.getElementById('fileInput12').files[0];
    let file3 = document.getElementById('fileInput13').files[0];
    let file4 = document.getElementById('fileInput14').files[0];

    isError = validateImage(fileVal1,fileVal2,fileVal3,fileVal4,file1,file2,file3 ,file4, $("#err11"), $("#err12"), $("#err13"), $("#err14"),formData,isError);
    //formData.append('files',new Blob($("#fileInput2").file));


    // formData.append("prod",JSON.stringify(productmodel));
    // DO POST

    if(!isError)
    {
        form.find("input[type='submit']").hide();
        form.find("div[class='addProg']").show();
        let productmodel = {
            id : $("#item0").val(),
            name : $("#item1").val(),
            description : $("#item2").val(),
            price : $("#item3").val(),
            imageUrl : fileVal1,
            imageUrl2 : fileVal2,
            imageUrl3 : fileVal3,
            imageUrl4 : fileVal4,
            categoryName :$(".cats").find(":selected").val()
        };
        let displayError=$("#EditError");
        updateServer(form,formData, productmodel, displayError,"PUT");

    }



}


function validateImage(fileVal1,fileVal2,fileVal3,fileVal4,file1,file2,file3 ,file4, err1, err2, err3, err4,formData,isError) {
    if (fileVal1.length > 0 && (fileVal1.toLowerCase().endsWith("jpg") || fileVal1.toLowerCase().endsWith("png") || fileVal1.toLowerCase().endsWith("jpeg")))
        formData.append('files', file1);
    else if (fileVal1.length > 0) {
        err1.text("This is not a valid image,you must use (Jpj,png) extensions");
        isError = true;
    }
    if (fileVal2.length > 0 && (fileVal2.toLowerCase().endsWith("jpg") || fileVal2.toLowerCase().endsWith("png") || fileVal2.toLowerCase().endsWith("jpeg")))
        formData.append('files', file2);
    else if (fileVal2.length > 0) {
        err2.text("This is not a valid image,you must use (Jpj,png) extensions");
        isError = true;
    }


    if (fileVal3.length > 0 && (fileVal3.toLowerCase().endsWith("jpg") || fileVal3.toLowerCase().endsWith("png") || fileVal3.toLowerCase().endsWith("jpeg")))
        formData.append('files', file3);
    else if (fileVal3.length > 0) {
        err3.text("This is not a valid image,you must use (Jpj,png) extensions");
        isError = true;
    }

    if (fileVal4.length > 0 && (fileVal4.toLowerCase().endsWith("jpg") || fileVal4.toLowerCase().endsWith("png") || fileVal4.toLowerCase().endsWith("jpeg")))
        formData.append('files', file4);
    else if (fileVal4.length > 0) {
        err4.text("This is not a valid image,you must use (Jpj,png) extensions");
        isError = true;
    }

        return isError;
}

function updateServer(form,formData, productmodel, displayError,methodType) {
    $.ajax({
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/productApi/image/create",
        contentType: false,
        cache: false,
        data: formData,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            $.ajax({
                type: methodType,
                contentType: "application/json",
                url: "/productApi/" + (methodType==="PUT"?"update":"create"),
                data: JSON.stringify(productmodel),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (result) {
                    form.find("input[type='submit']").show();
                    form.find("div[class='addProg']").hide();
                    getall();
                    form.parent().parent().parent().modal('hide');
                    console.log(result);
                },
                error: function (e) {
                    form.find("input[type='submit']").show();
                    form.find("div[class='addProg']").hide();
                    console.log("ERROR: ", e);
                    displayError.text(e.responseJSON.message);
                    displayError.show();
                }
            });
        }
    })
}






function DoDelet(){
    //let baseurl="http://adnanbk.herokuapp.com";

    $.ajax({
        type : "DELETE",
        //contentType : "application/json",
        url :"/productApi/delete/"+$("#item0").val(),
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
    let checkbox = $('table tbody input[type="checkbox"]');
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
    $("#EditError").hide();
    $("#AddError").hide();
/*    $("#err1").text("");
    $("#err2").text("");
    $("#err3").text("");
    $("#err4").text("");
    $("#add-item1").val("");
    $("#add-item2").val("");
    $("#add-item3").val("");
    $("#add-item4").val("");
    $("#add-item5").val("");
    $("#add-item6").val("");
    $("#add-item7").val("");*/
});

$("#paging").on('keyup', function (e) {
    if (e.keyCode == 13) {
        _page=$("#paging").val()-1;
        getall();
    }
});

