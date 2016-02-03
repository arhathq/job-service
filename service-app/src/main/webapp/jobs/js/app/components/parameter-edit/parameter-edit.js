can.Component.extend({
    tag: "parameter-edit",
    template: can.view('js/app/components/parameter-edit.stache'),
    viewModel: {
    },
    events: {
        "inserted": function(){
            console.log("parameter-edit inserted " + this.viewModel.attr('parameter'));
        }
    }
});