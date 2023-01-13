$(function () {

    let main = function () {
        initBuyBtn();
        $('#addToCart').click(addProductToCart);
        $('#addProductPopup .count').change(calculateCost);
        $('#loadMore').click(loadMoreProducts);
        $('#loadMoreMyOrders').click(loadMoreMyOrders);

        initSearchForm();
        $('#goSearch').click(goSearch);
        $('.remove-product').click(removeProductFromCart);
        $('.post-request').click(function () {
            postRequest($(this).attr('data-url'));
        });

    };

    let initBuyBtn = function () {
        $('.buy-btn').click(showAddProductPopup);
    };

    let showAddProductPopup = function () {
        let productId = $(this).attr('data-id-product');
        let product = $('#product' + productId);
        let productImg = product.find('.thumbnail img').attr('src');
        let productName = product.find('.name').text();
        let productCategory = product.find('.category').text();
        let productProducer = product.find('.producer').text();
        let productPrice = product.find('.price').text();

        $("#addProductPopup").attr('data-id-product', productId);
        $('#addProductPopup .product-image').attr('src', productImg);
        $('#addProductPopup .name').text(productName);
        $('#addProductPopup .category').text(productCategory);
        $('#addProductPopup .producer').text(productProducer);
        $('#addProductPopup .price').text(productPrice);
        $('#addProductPopup .count').val(1);
        $('#addProductPopup .cost').text(productPrice);

        $('#addToCartIndicator').addClass('hidden');
        $('#addToCart').removeClass('hidden');

        $('#addProductPopup').modal({
            show: true
        });
    };

    let addProductToCart = function () {
        let productId = $('#addProductPopup').attr('data-id-product');
        let count = $('#addProductPopup .count').val();

        let btn = $('#addToCart');
        convertButtonToLoader(btn, 'btn-primary')

        let url = '/ajax/json/product/add';
        $.ajax({
            url: url,
            method: 'POST',
            data: {
                idProduct: productId,
                count: count,
            },
            success: function (data) {
                $('#currentShoppingCart .total-count').text(data.totalCount);
                $('#currentShoppingCart .total-cost').text(data.totalCost);
                $('#currentShoppingCart').removeClass('hidden');
                convertLoaderToButton(btn, 'btn-primary', addProductToCart);
                $('#addProductPopup').modal('hide');
            },
            error: function (xhr) {
                convertLoaderToButton(btn, 'btn-primary', addProductToCart);
                if(xhr.status === 400) {
                    let jsonValue = (jQuery.parseJSON(xhr.responseText));
                    alert("Message: " + jsonValue.message);
                    // alert("StackTrace: " + jsonValue.StackTrace);
                    // alert("ExceptionType: " + jsonValue.);
                } else {
                    alert('Error');
                }

            }
        });
    };

    let calculateCost = function () {
        let productPriceStr = $('#addProductPopup .price').text();
        let productPrice = parseFloat(productPriceStr.replace('₽', ''));
        let productCount = parseInt($('#addProductPopup .count').val());
        let productCountMin = parseInt($('#addProductPopup .count').attr('min'));
        let productCountMax = parseInt($('#addProductPopup .count').attr('max'));
        if (productCount >= productCountMin && productCount <= productCountMax) {
            let productsCost = productPrice * productCount;
            $('#addProductPopup .cost').text(productsCost + '₽');
        } else {
            $('#addProductPopup .count').val(1);
            $('#addProductPopup .cost').text(productPriceStr);
        }
    };

    let loadMoreProducts = function () {
        let btn = $('#loadMore');
        convertButtonToLoader(btn, 'btn-success');
        let pageCurrentNumber = parseInt($('#productsList').attr('data-page-current-number'));
        let url = '/ajax/html/more' + location.pathname + '?page=' + (pageCurrentNumber + 1) + '&' + location.search.substring(1);
        $.ajax({
            url: url,
            success: function (html) {
                $('#productsList .row').append(html);
                pageCurrentNumber++;
                let pageCount = parseInt($('#productsList').attr('data-page-count'));
                $('#productsList').attr('data-page-current-number', pageCurrentNumber);
                if (pageCurrentNumber < pageCount) {
                    convertLoaderToButton(btn, 'btn-success', loadMoreProducts);
                } else {
                    btn.remove();
                }
                initBuyBtn();
            },
            error: function (data) {
                convertLoaderToButton(btn, 'btn-success', loadMoreProducts);
                alert('Error')
            }
        });
    };

    let loadMoreMyOrders = function() {
        let btn = $('#loadMoreMyOrders');
        convertButtonToLoader(btn, 'btn-success');
        let pageCurrentNumber = parseInt($('#myOrders').attr('data-page-current-number'));
        let url = '/ajax/html/more/my_orders?page=' + (pageCurrentNumber + 1);
        $.ajax({
            url: url,
            success: function (html) {
                $('#myOrders tbody').append(html);
                pageCurrentNumber++;
                let pageCount = parseInt($('#myOrders').attr('data-page-count'));
                $('#myOrders').attr('data-page-current-number', pageCurrentNumber);
                if (pageCurrentNumber < pageCount) {
                    convertLoaderToButton(btn, 'btn-success', loadMoreMyOrders);
                } else {
                    btn.remove();
                }
            },
            error: function (xhr) {
                convertLoaderToButton(btn, 'btn-success', loadMoreMyOrders);
                if(xhr.status === 401) {
                    window.location.href = '/sign_in';
                } else {
                    alert('Error')
                }
            }
        });
    };

    let convertButtonToLoader = function (btn, btnClass) {
        btn.removeClass(btnClass);
        btn.removeClass('btn');
        btn.addClass('load-indicator');
        let text = btn.text();
        btn.attr('date-btn-text', text);
        btn.text('');
        btn.off('click');
    };

    let convertLoaderToButton = function (btn, btnClass, actionClick) {
        btn.removeClass('load-indicator');
        btn.addClass('btn');
        btn.addClass(btnClass);
        btn.text(btn.attr('date-btn-text'));
        btn.removeAttr('date-btn-text');
        btn.click(actionClick);
    }

    let initSearchForm = function () {
        $('#allCategories').click(function () {
            $('.categories .search-option').prop('checked', $(this).is(':checked'));
        });

        $('.categories .search-option').click(function () {
            $('#allCategories').prop('checked', false);
        });

        $('#allProducers').click(function () {
            $('.producers .search-option').prop('checked', $(this).is(':checked'));
        });

        $('.producers .search-option').click(function () {
            $('#allProducers').prop('checked', false);
        });
    };

    let goSearch = function () {
        let isAllSelected = function (selector) {
            let uncheckedCounter = 0;
            $(selector).each(function (index, value) {
                if (!$(value).is(':checked')) {
                    uncheckedCounter++;
                }
            });
            return uncheckedCounter === 0;
        }

        if (isAllSelected('.categories .search-option')) {
            $('.categories .search-option').prop('checked', false);
        }
        if (isAllSelected('.producers .search-option')) {
            $('.producers .search-option').prop('checked', false);
        }

        $('form.search').submit();
    };

    let confirm = function (msg, okFunction) {
        if (window.confirm(msg))
            okFunction();
    };

    let removeProductFromCart = function () {
        let btn = $(this);
        confirm('Are you sure?', function () {
            executeRemoveProduct(btn);
        });
    };

    let refreshTotalCost = function () {
        let totalCost = 0;
        $('#shoppingCart .product-item').each(function (index, value) {
            let productCount = parseInt($(value).find('.count').text());
            let productPrice = parseFloat($(value).find('.price').text().replace('₽', ' '));
            totalCost += productCount * productPrice;
        })
        $('#shoppingCart .total-cost').text(totalCost + '₽');
    };

    let executeRemoveProduct = function (btn) {
        let productId = btn.attr('data-id-product');
        let productCountForRemoving = btn.attr('data-count');
        convertButtonToLoader(btn, 'btn-danger');

        let url = '/ajax/json/product/remove'
        $.ajax({
            url: url,
            method: 'POST',
            data: {
                idProduct: productId,
                count: productCountForRemoving,
            },
            success: function (data) {
                if (data.totalCount === 0) {
                    window.location.href = '/products'
                } else {
                    let idOfProductTableRowContainer = '#product' + productId;
                    let previousCount = parseInt($(idOfProductTableRowContainer + ' .count').text());
                    let removedCount = parseInt(productCountForRemoving);
                    if (removedCount >= previousCount) {
                        $(idOfProductTableRowContainer).remove();
                    } else {
                        convertLoaderToButton(btn, 'btn-danger', removeProductFromCart);
                        $(idOfProductTableRowContainer + ' .count').text(previousCount - removedCount);
                        if (previousCount - removedCount === 1) {
                            $(idOfProductTableRowContainer + ' a.remove-product.all').remove();
                        }
                    }
                    refreshTotalCost();
                }
            },
            error: function (data) {
                convertLoaderToButton(btn, 'btn-danger', removeProductFromCart);
                alert('Error');
            }
        });
    };

    let postRequest = function (url) {
        let form = '<form id="postRequestForm" action="' + url + '" method="post"></form>';
        $('body').append(form);
        $('#postRequestForm').submit();
    };

    main();
});
