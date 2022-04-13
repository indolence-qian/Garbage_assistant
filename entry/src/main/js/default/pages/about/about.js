import router from '@system.router';
export default {
    data: {
        text: '本项目为南昌航空大学的一般三小项目，开发者为20045322-钱芳贵及其小组成员。',
        text2: '怎么说呢，由于网上关于鸿蒙应用的资料较少再加上本人的0基础，在这个项目开发上真的走了很多的弯路，' +
        '有的时候一个小小的问题可以卡住我们几个星期。但是，最终它还是被成功的开发出来了，虽然它可能看起来不是那么的完美，' +
        '但是我们觉得只要是付出了努力，它就是完美的，通过这个项目我们学到了很多。' +
        '这其中也要感谢小组成员的大力支持！,正是大家的一致努力才有了今天的项目。',
        text3: '小组成员:20045322-钱芳贵，20045311-金磊，20045325-涂尔轩。                                                  ' +
        ' 感谢所有对本作品给予支持的小组成员和看到使用这个项目的使用者们！',
        text4: "",
        text5: ""
    },
    back() {
        router.back();
    },
    about() {
        this.$app.$def.data.caidan++;
        if(this.$app.$def.data.caidan%2==1) {
            this.text5 = this.text2;
        }
        if(this.$app.$def.data.caidan%2==0) {
            this.text5 = this.text4;
        }
    }
}
