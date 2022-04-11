import prompt from '@system.prompt';
export default {
    data: {
        title: 'World',
        album_array: [],
    },
    onInit() {
        console.info('plus result is:' + JSON.stringify(this.$app.$def.data.album_array));
        for(var i=0;i<this.$app.$def.data.album_array.length;i++) {
            this.album_array.push({
                img: this.$app.$def.data.album_array[i],
                choose: false
            });
        }
        console.info('plus result is:' + JSON.stringify(this.album_array));
    },
    select(e)
    {
        prompt.showToast({
            message: 'select ' + e.currentTarget.value
        })
    },
    confirm(e)
    {

//prompt.showToast({
//            message: 'select ' + e
//        })
        var s="";
        for(var i=0;i<this.album_array.length;i++) {
            if(this.album_array[i].choose)
            {
                s=this.album_array[i].img;
            }
        }
        prompt.showToast({
            message: 'select ' + s
        })
    }
}
