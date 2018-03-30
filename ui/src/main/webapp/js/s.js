$(document).ready(function() {
    $("#logoutLink").click(function() {
        $("#logoutForm").submit();
        return false;
    });

    $("#filterToggleBtn").click(function() {
        $("#filterPanel").toggleClass("hidden", "fast", "swing");
    });
});

var DATA_TABLE_PROPS = {
    sDom: "rtip",
    order: [[ 0, "desc" ]] //sort by id column
};

function nx_init(options) {
    $("#resetBtn").click(function() {
        window.location.replace(options.default_url);
    });

    var propTableTemplate = $("#prop-table-template").html();
    Mustache.parse(propTableTemplate);

    $('#detailsModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var id = button.data('id');
        if (id == undefined) {
            if (options.table) {
                var d = options.table.row($(button).parents("tr")).data();
                id = d["id"];
            }
        }
        var modal = $(this);
        $.ajax(options.default_url + "/" + id, {
            dataType: "json",
            before: function() {
                $("#fieldsTable").hide();
            },
            complete: function() {
                $("#fieldsTable").show();
            },
            success: function(data, status, xhr) {
                $("#prop-table-target").html(Mustache.render(propTableTemplate, {"items": data}));

                if (options.modal_rendered_callback) {
                    options.modal_rendered_callback();
                }
            }
        });
        modal.data("id", id);
        modal.find('.modal-title').text('Details of object #' + id);
        modal.find('.modal-body input').val(id);
    });

    //vm: this script isn't working well with htmlunit
    $("input.datetime").datetimepicker({
         "format": 'dd/mm/yyyy hh:ii'
    });
}

//vm: fix for spring mvc, see http://stackoverflow.com/questions/5900840/post-nested-object-to-spring-mvc-controller-using-json
//vm: copied from jQuery and modified to our needs
(function(jQuery) {
    var r20 = /%20/g,
        rbracket = /\[\]$/,
        rCRLF = /\r?\n/g,
        rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
        rsubmittable = /^(?:input|select|textarea|keygen)/i;

    function buildParams( prefix, obj, traditional, add ) {
        var name;

        if ( jQuery.isArray( obj ) ) {
            // Serialize array item.
            jQuery.each( obj, function( i, v ) {
                if ( traditional || rbracket.test( prefix ) ) {
                    // Treat each array item as a scalar.
                    add( prefix, v );

                } else {
                    // Item is non-scalar (array or object), encode its numeric index.
                    buildParams(
                            prefix + "[" + ( typeof v === "object" ? i : "" ) + "]",
                        v,
                        traditional,
                        add
                    );
                }
            });

        } else if ( !traditional && jQuery.type( obj ) === "object" ) {
            // Serialize object item.
            for ( name in obj ) {
                buildParams( prefix + "." + name , obj[ name ], traditional, add );
            }

        } else {
            // Serialize scalar item.
            add( prefix, obj );
        }
    }

// Serialize an array of form elements or a set of
// key/values into a query string
    jQuery.param = function( a, traditional ) {
        var prefix,
            s = [],
            add = function( key, value ) {
                // If value is a function, invoke it and return its value
                value = jQuery.isFunction( value ) ? value() : ( value == null ? "" : value );
                s[ s.length ] = encodeURIComponent( key ) + "=" + encodeURIComponent( value );
            };

        // Set traditional to true for jQuery <= 1.3.2 behavior.
        if ( traditional === undefined ) {
            traditional = jQuery.ajaxSettings && jQuery.ajaxSettings.traditional;
        }

        // If an array was passed in, assume that it is an array of form elements.
        if ( jQuery.isArray( a ) || ( a.jquery && !jQuery.isPlainObject( a ) ) ) {
            // Serialize the form elements
            jQuery.each( a, function() {
                add( this.name, this.value );
            });

        } else {
            // If traditional, encode the "old" way (the way 1.3.2 or older
            // did it), otherwise encode params recursively.
            for ( prefix in a ) {
                buildParams( prefix, a[ prefix ], traditional, add );
            }
        }

        // Return the resulting serialization
        return s.join( "&" ).replace( r20, "+" );
    };
})(jQuery);
