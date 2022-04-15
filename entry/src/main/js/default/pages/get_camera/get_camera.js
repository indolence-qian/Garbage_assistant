import router from '@system.router';
import http from '@ohos.net.http';
import mediaLibrary from '@ohos.multimedia.mediaLibrary';
import md5 from 'blueimp-md5'
export default {
    data: {
        title: 'World',
        flash: "off",
        direction: "back",
        photoUri: ""
    },
    flashlight() {
        if(this.flash=="off"){
            this.flash="torch";
        }
        else {
            this.flash="off";
        }
    },
    reverse() {
        if(this.direction=="back") {
            this.direction="front";
        }
        else {
            this.direction="back";
        }
    },
    shoot() {
        console.info("camera success!");
        this.$element('cameraApp').takePhoto({
            quality: "normal",
            success: (res) => {
                console.info("  hahaha     " + res.uri);
                this.photoUri = res.uri;
                let option = {
                    src: this.photoUri,
                    mimeType: "image/jpeg",
                    relativePath: "imageDir/image2/"
                };

                mediaLibrary.getMediaLibrary().storeMediaAsset(option).then((value) => {
                    console.info("Media resources stored."+value);
                    // Obtain the URI that stores media resources.
                    router.push({
                        uri: 'pages/result/result',
                        params: {
                            uri: value,
                        }
                    })
                }).catch((err) => {
                    console.info("An error occurred when storing media resources.");
                });
                console.info("success!");

            },
            fail: (erromsg, errocode) => {
                console.log('media.takePhoto----------' + errocode + ': ' + erromsg)
            }
        })
    }
}