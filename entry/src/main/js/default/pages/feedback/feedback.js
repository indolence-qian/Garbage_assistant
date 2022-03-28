import document from '@ohos.document';
import prompt from '@system.prompt';
export default {
    data: {
        title: 'World'
    },
    get_text(e) {
        const filename = '临时日志';
        const qhyhLog = e.value; // 需要导出的字符串
        prompt.showToast({message:e.value});
        const element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(qhyhLog));
        element.setAttribute('download', filename);
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    },
}
