package com.lxw.videoworld.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zion on 2017/11/25.
 */

public class MaoYanMovieModel implements Serializable {


    /**
     * success : true
     * data : {"updateInfo":"北京时间 19:53:07","totalBoxUnitInfo":"万","splitTotalBox":"12941.8","serverTimestamp":1511610787000,"crystal":{"status":0},"totalBoxInfo":"13751.3","list":[{"showInfo":"43687","boxRate":"30.7%","releaseInfo":"上映2天","avgShowView":"29","sumBoxInfo":"5500.4万","splitBoxRate":"30.4%","seatRate":"14.7%","movieId":342068,"splitBoxInfo":"3941.77","avgSeatView":"24.7%","viewInfo":"122.6","showRate":"15.3%","releaseInfoColor":"#666666 1.00","boxInfo":"4222.54","avgViewBox":"34.4","splitAvgViewBox":"32.2","movieName":"寻梦环游记","onlineBoxRate":"--","splitSumBoxInfo":"5134.8万"},{"showInfo":"58435","boxRate":"28.5%","releaseInfo":"上映9天","avgShowView":"20","sumBoxInfo":"5.11亿","splitBoxRate":"28.4%","seatRate":"26.2%","movieId":341195,"splitBoxInfo":"3680.95","avgSeatView":"13.2%","viewInfo":"111.7","showRate":"20.4%","releaseInfoColor":"#666666 1.00","boxInfo":"3923.52","milestone":{"box":50000000000,"boxCopy":"破5亿","dateTime":1511601700000,"imageUrl":"https://img.meituan.net/pro/73b8973160be5fd60982305ec73b07ad420362.jpg","movieId":341195},"avgViewBox":"35.1","splitAvgViewBox":"32.9","movieName":"正义联盟","onlineBoxRate":"--","splitSumBoxInfo":"4.79亿"},{"showInfo":"75336","boxRate":"15.0%","releaseInfo":"上映2天","avgShowView":"9","sumBoxInfo":"5057.3万","splitBoxRate":"15.2%","seatRate":"28.7%","movieId":248075,"splitBoxInfo":"1973.28","avgSeatView":"8.4%","viewInfo":"64.8","showRate":"26.3%","releaseInfoColor":"#666666 1.00","boxInfo":"2067.31","avgViewBox":"31.9","splitAvgViewBox":"30.4","movieName":"追捕","onlineBoxRate":"--","splitSumBoxInfo":"4827.5万"},{"showInfo":"39548","boxRate":"7.1%","releaseInfo":"上映2天","avgShowView":"9","sumBoxInfo":"2078.1万","splitBoxRate":"7.1%","seatRate":"11.8%","movieId":1196171,"splitBoxInfo":"914.30","avgSeatView":"9.9%","viewInfo":"34.4","showRate":"13.8%","releaseInfoColor":"#666666 1.00","boxInfo":"976.96","avgViewBox":"28.4","splitAvgViewBox":"26.6","movieName":"引爆者","onlineBoxRate":"--","splitSumBoxInfo":"1945.4万"},{"showInfo":"10292","boxRate":"3.5%","releaseInfo":"上映2天","avgShowView":"15","sumBoxInfo":"718.0万","splitBoxRate":"3.5%","seatRate":"2.9%","movieId":1208113,"splitBoxInfo":"450.61","avgSeatView":"16.5%","viewInfo":"14.9","showRate":"3.6%","releaseInfoColor":"#666666 1.00","boxInfo":"485.58","avgViewBox":"32.5","splitAvgViewBox":"30.1","movieName":"嘉年华","onlineBoxRate":"--","splitSumBoxInfo":"667.3万"},{"showInfo":"3529","boxRate":"2.3%","releaseInfo":"上映16天","avgShowView":"27","sumBoxInfo":"2.17亿","splitBoxRate":"2.2%","seatRate":"1.0%","movieId":344422,"splitBoxInfo":"288.32","avgSeatView":"26.0%","viewInfo":"9.4","showRate":"1.2%","releaseInfoColor":"#666666 1.00","boxInfo":"311.61","avgViewBox":"33.0","splitAvgViewBox":"30.6","movieName":"东方快车谋杀案","onlineBoxRate":"--","splitSumBoxInfo":"2.01亿"},{"showInfo":"4690","boxRate":"2.0%","releaseInfo":"上映23天","avgShowView":"18","sumBoxInfo":"7.31亿","splitBoxRate":"2.0%","seatRate":"1.3%","movieId":249894,"splitBoxInfo":"263.99","avgSeatView":"18.6%","viewInfo":"8.2","showRate":"1.6%","releaseInfoColor":"#666666 1.00","boxInfo":"282.57","avgViewBox":"34.4","splitAvgViewBox":"32.1","movieName":"雷神3：诸神黄昏","onlineBoxRate":"--","splitSumBoxInfo":"6.83亿"},{"showInfo":"19696","boxRate":"1.7%","releaseInfo":"上映2天","avgShowView":"5","sumBoxInfo":"677.9万","splitBoxRate":"1.8%","seatRate":"5.2%","movieId":1190680,"splitBoxInfo":"227.14","avgSeatView":"7.4%","viewInfo":"8.7","showRate":"6.9%","releaseInfoColor":"#666666 1.00","boxInfo":"234.33","avgViewBox":"26.7","splitAvgViewBox":"25.9","movieName":"推理笔记","onlineBoxRate":"--","splitSumBoxInfo":"659.8万"},{"showInfo":"6596","boxRate":"1.2%","releaseInfo":"上映9天","avgShowView":"8","sumBoxInfo":"8333.8万","splitBoxRate":"1.3%","seatRate":"1.9%","movieId":246019,"splitBoxInfo":"163.28","avgSeatView":"9.2%","viewInfo":"5.0","showRate":"2.3%","releaseInfoColor":"#666666 1.00","boxInfo":"172.59","avgViewBox":"33.9","splitAvgViewBox":"32.1","movieName":"降魔传","onlineBoxRate":"--","splitSumBoxInfo":"7968.9万"},{"showInfo":"2163","boxRate":"0.9%","releaseInfo":"上映23天","avgShowView":"18","sumBoxInfo":"8124.0万","splitBoxRate":"0.9%","seatRate":"0.5%","movieId":618704,"splitBoxInfo":"114.64","avgSeatView":"18.3%","viewInfo":"3.7","showRate":"0.8%","releaseInfoColor":"#666666 1.00","boxInfo":"122.49","avgViewBox":"33.1","splitAvgViewBox":"31.0","movieName":"七十七天","onlineBoxRate":"--","splitSumBoxInfo":"7546.1万"},{"showInfo":"708","boxRate":"0.7%","releaseInfo":"上映3天","avgShowView":"42","sumBoxInfo":"400.1万","splitBoxRate":"0.8%","seatRate":"0.2%","movieId":1211004,"splitBoxInfo":"102.17","avgSeatView":"33.6%","viewInfo":"2.9","showRate":"0.2%","releaseInfoColor":"#666666 1.00","boxInfo":"102.55","avgViewBox":"35.0","splitAvgViewBox":"34.9","movieName":"一路绽放","onlineBoxRate":"--","splitSumBoxInfo":"398.4万"},{"showInfo":"6416","boxRate":"0.5%","releaseInfo":"上映2天","avgShowView":"4","sumBoxInfo":"197.4万","splitBoxRate":"0.5%","seatRate":"1.7%","movieId":342276,"splitBoxInfo":"69.87","avgSeatView":"7.7%","viewInfo":"2.3","showRate":"2.2%","releaseInfoColor":"#666666 1.00","boxInfo":"72.70","avgViewBox":"31.0","splitAvgViewBox":"29.8","movieName":"刺杀盖世太保","onlineBoxRate":"--","splitSumBoxInfo":"189.8万"},{"showInfo":"1015","boxRate":"0.5%","releaseInfo":"上映5天","avgShowView":"22","sumBoxInfo":"549.0万","splitBoxRate":"0.5%","seatRate":"0.2%","movieId":343476,"splitBoxInfo":"64.52","avgSeatView":"19.9%","viewInfo":"2.1","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"68.66","avgViewBox":"31.8","splitAvgViewBox":"29.8","movieName":"不成问题的问题","onlineBoxRate":"--","splitSumBoxInfo":"512.9万"},{"showInfo":"1273","boxRate":"0.5%","releaseInfo":"上映57天","avgShowView":"16","sumBoxInfo":"22.03亿","splitBoxRate":"0.5%","seatRate":"0.3%","movieId":1198214,"splitBoxInfo":"61.67","avgSeatView":"17.0%","viewInfo":"2.0","showRate":"0.4%","releaseInfoColor":"#666666 1.00","boxInfo":"66.26","avgViewBox":"32.7","splitAvgViewBox":"30.5","movieName":"羞羞的铁拳","onlineBoxRate":"--","splitSumBoxInfo":"20.58亿"},{"showInfo":"1826","boxRate":"0.3%","releaseInfo":"上映9天","avgShowView":"7","sumBoxInfo":"3107.9万","splitBoxRate":"0.3%","seatRate":"0.4%","movieId":313,"splitBoxInfo":"34.01","avgSeatView":"9.6%","viewInfo":"1.1","showRate":"0.6%","releaseInfoColor":"#666666 1.00","boxInfo":"36.36","avgViewBox":"32.0","splitAvgViewBox":"29.9","movieName":"英雄本色","onlineBoxRate":"--","splitSumBoxInfo":"2890.0万"},{"showInfo":"768","boxRate":"0.2%","releaseInfo":"上映9天","avgShowView":"12","sumBoxInfo":"1992.0万","splitBoxRate":"0.2%","seatRate":"0.2%","movieId":248268,"splitBoxInfo":"27.15","avgSeatView":"13.0%","viewInfo":"0.9","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"29.00","avgViewBox":"31.9","splitAvgViewBox":"29.8","movieName":"恐袭波士顿","onlineBoxRate":"--","splitSumBoxInfo":"1847.4万"},{"showInfo":"52","boxRate":"0.2%","releaseInfo":"上映16天","avgShowView":"85","sumBoxInfo":"2722.4万","splitBoxRate":"0.2%","seatRate":"<0.1%","movieId":343985,"splitBoxInfo":"25.19","avgSeatView":"52.1%","viewInfo":"0.4","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"25.21","avgViewBox":"57.2","splitAvgViewBox":"57.2","movieName":"烽火芳菲","onlineBoxRate":"--","splitSumBoxInfo":"2673.0万"},{"showInfo":"3871","boxRate":"0.2%","releaseInfo":"上映2天","avgShowView":"3","sumBoxInfo":"118.7万","splitBoxRate":"0.2%","seatRate":"1.0%","movieId":1133164,"splitBoxInfo":"22.60","avgSeatView":"4.7%","viewInfo":"0.9","showRate":"1.4%","releaseInfoColor":"#666666 1.00","boxInfo":"24.23","avgViewBox":"26.0","splitAvgViewBox":"24.2","movieName":"七月半3：灵触第七感","onlineBoxRate":"--","splitSumBoxInfo":"110.6万"},{"showInfo":"142","boxRate":"0.1%","releaseInfo":"2014-08","avgShowView":"45","sumBoxInfo":"20.7万","splitBoxRate":"0.1%","seatRate":"<0.1%","movieId":246131,"splitBoxInfo":"19.08","avgSeatView":"7.2%","viewInfo":"0.6","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"20.67","avgViewBox":"32.5","splitAvgViewBox":"30.0","movieName":"泡菜","onlineBoxRate":"--","splitSumBoxInfo":"19.1万"},{"showInfo":"838","boxRate":"0.1%","releaseInfo":"上映9天","avgShowView":"7","sumBoxInfo":"2652.3万","splitBoxRate":"0.1%","seatRate":"0.2%","movieId":346511,"splitBoxInfo":"17.66","avgSeatView":"9.7%","viewInfo":"0.5","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"18.69","avgViewBox":"31.9","splitAvgViewBox":"30.1","movieName":"暴雪将至","onlineBoxRate":"--","splitSumBoxInfo":"2481.4万"},{"showInfo":"911","boxRate":"0.1%","releaseInfo":"上映15天","avgShowView":"8","sumBoxInfo":"2504.8万","splitBoxRate":"0.1%","seatRate":"0.2%","movieId":617007,"splitBoxInfo":"17.47","avgSeatView":"12.0%","viewInfo":"0.6","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"18.28","avgViewBox":"27.1","splitAvgViewBox":"25.9","movieName":"精灵宝可梦：波尔凯尼恩与机巧的玛机雅娜","onlineBoxRate":"--","splitSumBoxInfo":"2319.1万"},{"showInfo":"175","boxRate":"<0.1%","releaseInfo":"上映23天","avgShowView":"20","sumBoxInfo":"1771.0万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":346641,"splitBoxInfo":"12.57","avgSeatView":"27.2%","viewInfo":"0.3","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"13.31","avgViewBox":"38.5","splitAvgViewBox":"36.3","movieName":"相爱相亲","onlineBoxRate":"--","splitSumBoxInfo":"1666.2万"},{"showInfo":"311","boxRate":"<0.1%","releaseInfo":"上映30天","avgShowView":"11","sumBoxInfo":"4.32亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":338402,"splitBoxInfo":"10.48","avgSeatView":"17.4%","viewInfo":"0.3","showRate":"0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"11.22","avgViewBox":"33.3","splitAvgViewBox":"31.1","movieName":"全球风暴","onlineBoxRate":"--","splitSumBoxInfo":"4.03亿"},{"showInfo":"421","boxRate":"<0.1%","releaseInfo":"上映43天","avgShowView":"10","sumBoxInfo":"5577.6万","splitBoxRate":"<0.1%","seatRate":"0.1%","movieId":880690,"splitBoxInfo":"9.23","avgSeatView":"15.6%","viewInfo":"0.3","showRate":"0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"9.83","avgViewBox":"25.3","splitAvgViewBox":"23.7","movieName":"我的爸爸是森林之王","onlineBoxRate":"--","splitSumBoxInfo":"5178.0万"},{"showInfo":"220","boxRate":"<0.1%","releaseInfo":"上映58天","avgShowView":"15","sumBoxInfo":"4.57亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1198177,"splitBoxInfo":"8.96","avgSeatView":"16.0%","viewInfo":"0.3","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"9.63","avgViewBox":"30.7","splitAvgViewBox":"28.6","movieName":"缝纫机乐队","onlineBoxRate":"--","splitSumBoxInfo":"4.29亿"},{"showInfo":"508","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"6","sumBoxInfo":"26.4万","splitBoxRate":"<0.1%","seatRate":"0.1%","movieId":246503,"splitBoxInfo":"8.47","avgSeatView":"13.8%","viewInfo":"0.2","showRate":"0.2%","releaseInfoColor":"#666666 1.00","boxInfo":"8.71","avgViewBox":"31.6","splitAvgViewBox":"30.7","movieName":"灿烂这一刻","onlineBoxRate":"--","splitSumBoxInfo":"25.7万"},{"showInfo":"219","boxRate":"<0.1%","releaseInfo":"上映16天","avgShowView":"7","sumBoxInfo":"2366.1万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":342193,"splitBoxInfo":"3.78","avgSeatView":"16.9%","viewInfo":"0.1","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"4.07","avgViewBox":"30.2","splitAvgViewBox":"28.1","movieName":"天生不对","onlineBoxRate":"--","splitSumBoxInfo":"2207.6万"},{"showInfo":"135","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"10","sumBoxInfo":"7.6万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1210973,"splitBoxInfo":"3.08","avgSeatView":"37.0%","viewInfo":"0.1","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"3.14","avgViewBox":"25.9","splitAvgViewBox":"25.3","movieName":"北纬二十八度","onlineBoxRate":"--","splitSumBoxInfo":"7.5万"},{"showInfo":"49","boxRate":"<0.1%","releaseInfo":"上映30天","avgShowView":"15","sumBoxInfo":"7688.3万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":247784,"splitBoxInfo":"2.85","avgSeatView":"22.3%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"3.06","avgViewBox":"44.6","splitAvgViewBox":"41.5","movieName":"银翼杀手2049","onlineBoxRate":"--","splitSumBoxInfo":"7241.1万"},{"showInfo":"282","boxRate":"<0.1%","releaseInfo":"上映9天","avgShowView":"4","sumBoxInfo":"343.9万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":635848,"splitBoxInfo":"2.44","avgSeatView":"6.8%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.62","avgViewBox":"27.2","splitAvgViewBox":"25.2","movieName":"深宫怨灵","onlineBoxRate":"--","splitSumBoxInfo":"316.7万"},{"showInfo":"353","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"4","sumBoxInfo":"11.9万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1211081,"splitBoxInfo":"2.40","avgSeatView":"28.8%","viewInfo":"0.1","showRate":"0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.60","avgViewBox":"21.7","splitAvgViewBox":"20.0","movieName":"炸裂青春","onlineBoxRate":"--","splitSumBoxInfo":"11.2万"},{"showInfo":"72","boxRate":"<0.1%","releaseInfo":"上映23天","avgShowView":"12","sumBoxInfo":"7917.7万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":338328,"splitBoxInfo":"2.51","avgSeatView":"18.3%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.58","avgViewBox":"30.2","splitAvgViewBox":"29.3","movieName":"密战","onlineBoxRate":"--","splitSumBoxInfo":"7398.8万"},{"showInfo":"14","boxRate":"<0.1%","releaseInfo":"上映9天","avgShowView":"55","sumBoxInfo":"14.7万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1207927,"splitBoxInfo":"2.27","avgSeatView":"33.2%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.46","avgViewBox":"32.5","splitAvgViewBox":"30.0","movieName":"一辈子的姐弟","onlineBoxRate":"--","splitSumBoxInfo":"14.4万"},{"showInfo":"67","boxRate":"<0.1%","releaseInfo":"上映57天","avgShowView":"21","sumBoxInfo":"5.75亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1176763,"splitBoxInfo":"1.99","avgSeatView":"19.9%","viewInfo":"0.1","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.24","avgViewBox":"16.7","splitAvgViewBox":"14.8","movieName":"追龙","onlineBoxRate":"--","splitSumBoxInfo":"5.37亿"},{"showInfo":"12","boxRate":"<0.1%","releaseInfo":"上映40天","avgShowView":"40","sumBoxInfo":"1128.7万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1208031,"splitBoxInfo":"2.21","avgSeatView":"14.4%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.21","avgViewBox":"46.2","splitAvgViewBox":"46.2","movieName":"你若安好","onlineBoxRate":"--","splitSumBoxInfo":"1128.1万"},{"showInfo":"4","boxRate":"<0.1%","releaseInfo":"2017-06","avgShowView":"129","sumBoxInfo":"7362.2万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1197625,"splitBoxInfo":"1.71","avgSeatView":"57.4%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.71","avgViewBox":"33.4","splitAvgViewBox":"33.4","movieName":"血战湘江","onlineBoxRate":"--","splitSumBoxInfo":"7343.0万"},{"showInfo":"8","boxRate":"<0.1%","releaseInfo":"点映","avgShowView":"61","sumBoxInfo":"6.0万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":672281,"splitBoxInfo":"1.45","avgSeatView":"77.8%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#FF9900 1.00","boxInfo":"1.57","avgViewBox":"32.5","splitAvgViewBox":"30.0","movieName":"这就是命","onlineBoxRate":"--","splitSumBoxInfo":"5.7万"},{"showInfo":"581","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"1","sumBoxInfo":"4.2万","splitBoxRate":"<0.1%","seatRate":"0.1%","movieId":337727,"splitBoxInfo":"1.46","avgSeatView":"8.5%","viewInfo":"0.0","showRate":"0.2%","releaseInfoColor":"#666666 1.00","boxInfo":"1.53","avgViewBox":"35.2","splitAvgViewBox":"33.7","movieName":"女巫斗恶龙","onlineBoxRate":"--","splitSumBoxInfo":"4.1万"},{"showInfo":"150","boxRate":"<0.1%","releaseInfo":"上映16天","avgShowView":"3","sumBoxInfo":"6741.9万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":344500,"splitBoxInfo":"1.27","avgSeatView":"5.3%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.35","avgViewBox":"34.7","splitAvgViewBox":"32.9","movieName":"狂兽","onlineBoxRate":"--","splitSumBoxInfo":"6428.6万"},{"showInfo":"35","boxRate":"<0.1%","releaseInfo":"上映44天","avgShowView":"12","sumBoxInfo":"2.70亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1204596,"splitBoxInfo":"1.17","avgSeatView":"8.8%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.29","avgViewBox":"32.3","splitAvgViewBox":"29.3","movieName":"天才枪手","onlineBoxRate":"--","splitSumBoxInfo":"2.49亿"},{"showInfo":"10","boxRate":"<0.1%","releaseInfo":"上映44天","avgShowView":"48","sumBoxInfo":"1.06亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1209085,"splitBoxInfo":"1.23","avgSeatView":"69.6%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.23","avgViewBox":"26.3","splitAvgViewBox":"26.2","movieName":"十八洞村","onlineBoxRate":"--","splitSumBoxInfo":"1.06亿"}],"totalBoxUnit":"万","totalBox":"13751.3","splitTotalBoxUnit":"万","queryDate":"2017-11-25","serverTime":"2017-11-25 19:53:07","splitTotalBoxUnitInfo":"万","splitTotalBoxInfo":"12941.8"}
     */

    private boolean success;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * updateInfo : 北京时间 19:53:07
         * totalBoxUnitInfo : 万
         * splitTotalBox : 12941.8
         * serverTimestamp : 1511610787000
         * crystal : {"status":0}
         * totalBoxInfo : 13751.3
         * list : [{"showInfo":"43687","boxRate":"30.7%","releaseInfo":"上映2天","avgShowView":"29","sumBoxInfo":"5500.4万","splitBoxRate":"30.4%","seatRate":"14.7%","movieId":342068,"splitBoxInfo":"3941.77","avgSeatView":"24.7%","viewInfo":"122.6","showRate":"15.3%","releaseInfoColor":"#666666 1.00","boxInfo":"4222.54","avgViewBox":"34.4","splitAvgViewBox":"32.2","movieName":"寻梦环游记","onlineBoxRate":"--","splitSumBoxInfo":"5134.8万"},{"showInfo":"58435","boxRate":"28.5%","releaseInfo":"上映9天","avgShowView":"20","sumBoxInfo":"5.11亿","splitBoxRate":"28.4%","seatRate":"26.2%","movieId":341195,"splitBoxInfo":"3680.95","avgSeatView":"13.2%","viewInfo":"111.7","showRate":"20.4%","releaseInfoColor":"#666666 1.00","boxInfo":"3923.52","milestone":{"box":50000000000,"boxCopy":"破5亿","dateTime":1511601700000,"imageUrl":"https://img.meituan.net/pro/73b8973160be5fd60982305ec73b07ad420362.jpg","movieId":341195},"avgViewBox":"35.1","splitAvgViewBox":"32.9","movieName":"正义联盟","onlineBoxRate":"--","splitSumBoxInfo":"4.79亿"},{"showInfo":"75336","boxRate":"15.0%","releaseInfo":"上映2天","avgShowView":"9","sumBoxInfo":"5057.3万","splitBoxRate":"15.2%","seatRate":"28.7%","movieId":248075,"splitBoxInfo":"1973.28","avgSeatView":"8.4%","viewInfo":"64.8","showRate":"26.3%","releaseInfoColor":"#666666 1.00","boxInfo":"2067.31","avgViewBox":"31.9","splitAvgViewBox":"30.4","movieName":"追捕","onlineBoxRate":"--","splitSumBoxInfo":"4827.5万"},{"showInfo":"39548","boxRate":"7.1%","releaseInfo":"上映2天","avgShowView":"9","sumBoxInfo":"2078.1万","splitBoxRate":"7.1%","seatRate":"11.8%","movieId":1196171,"splitBoxInfo":"914.30","avgSeatView":"9.9%","viewInfo":"34.4","showRate":"13.8%","releaseInfoColor":"#666666 1.00","boxInfo":"976.96","avgViewBox":"28.4","splitAvgViewBox":"26.6","movieName":"引爆者","onlineBoxRate":"--","splitSumBoxInfo":"1945.4万"},{"showInfo":"10292","boxRate":"3.5%","releaseInfo":"上映2天","avgShowView":"15","sumBoxInfo":"718.0万","splitBoxRate":"3.5%","seatRate":"2.9%","movieId":1208113,"splitBoxInfo":"450.61","avgSeatView":"16.5%","viewInfo":"14.9","showRate":"3.6%","releaseInfoColor":"#666666 1.00","boxInfo":"485.58","avgViewBox":"32.5","splitAvgViewBox":"30.1","movieName":"嘉年华","onlineBoxRate":"--","splitSumBoxInfo":"667.3万"},{"showInfo":"3529","boxRate":"2.3%","releaseInfo":"上映16天","avgShowView":"27","sumBoxInfo":"2.17亿","splitBoxRate":"2.2%","seatRate":"1.0%","movieId":344422,"splitBoxInfo":"288.32","avgSeatView":"26.0%","viewInfo":"9.4","showRate":"1.2%","releaseInfoColor":"#666666 1.00","boxInfo":"311.61","avgViewBox":"33.0","splitAvgViewBox":"30.6","movieName":"东方快车谋杀案","onlineBoxRate":"--","splitSumBoxInfo":"2.01亿"},{"showInfo":"4690","boxRate":"2.0%","releaseInfo":"上映23天","avgShowView":"18","sumBoxInfo":"7.31亿","splitBoxRate":"2.0%","seatRate":"1.3%","movieId":249894,"splitBoxInfo":"263.99","avgSeatView":"18.6%","viewInfo":"8.2","showRate":"1.6%","releaseInfoColor":"#666666 1.00","boxInfo":"282.57","avgViewBox":"34.4","splitAvgViewBox":"32.1","movieName":"雷神3：诸神黄昏","onlineBoxRate":"--","splitSumBoxInfo":"6.83亿"},{"showInfo":"19696","boxRate":"1.7%","releaseInfo":"上映2天","avgShowView":"5","sumBoxInfo":"677.9万","splitBoxRate":"1.8%","seatRate":"5.2%","movieId":1190680,"splitBoxInfo":"227.14","avgSeatView":"7.4%","viewInfo":"8.7","showRate":"6.9%","releaseInfoColor":"#666666 1.00","boxInfo":"234.33","avgViewBox":"26.7","splitAvgViewBox":"25.9","movieName":"推理笔记","onlineBoxRate":"--","splitSumBoxInfo":"659.8万"},{"showInfo":"6596","boxRate":"1.2%","releaseInfo":"上映9天","avgShowView":"8","sumBoxInfo":"8333.8万","splitBoxRate":"1.3%","seatRate":"1.9%","movieId":246019,"splitBoxInfo":"163.28","avgSeatView":"9.2%","viewInfo":"5.0","showRate":"2.3%","releaseInfoColor":"#666666 1.00","boxInfo":"172.59","avgViewBox":"33.9","splitAvgViewBox":"32.1","movieName":"降魔传","onlineBoxRate":"--","splitSumBoxInfo":"7968.9万"},{"showInfo":"2163","boxRate":"0.9%","releaseInfo":"上映23天","avgShowView":"18","sumBoxInfo":"8124.0万","splitBoxRate":"0.9%","seatRate":"0.5%","movieId":618704,"splitBoxInfo":"114.64","avgSeatView":"18.3%","viewInfo":"3.7","showRate":"0.8%","releaseInfoColor":"#666666 1.00","boxInfo":"122.49","avgViewBox":"33.1","splitAvgViewBox":"31.0","movieName":"七十七天","onlineBoxRate":"--","splitSumBoxInfo":"7546.1万"},{"showInfo":"708","boxRate":"0.7%","releaseInfo":"上映3天","avgShowView":"42","sumBoxInfo":"400.1万","splitBoxRate":"0.8%","seatRate":"0.2%","movieId":1211004,"splitBoxInfo":"102.17","avgSeatView":"33.6%","viewInfo":"2.9","showRate":"0.2%","releaseInfoColor":"#666666 1.00","boxInfo":"102.55","avgViewBox":"35.0","splitAvgViewBox":"34.9","movieName":"一路绽放","onlineBoxRate":"--","splitSumBoxInfo":"398.4万"},{"showInfo":"6416","boxRate":"0.5%","releaseInfo":"上映2天","avgShowView":"4","sumBoxInfo":"197.4万","splitBoxRate":"0.5%","seatRate":"1.7%","movieId":342276,"splitBoxInfo":"69.87","avgSeatView":"7.7%","viewInfo":"2.3","showRate":"2.2%","releaseInfoColor":"#666666 1.00","boxInfo":"72.70","avgViewBox":"31.0","splitAvgViewBox":"29.8","movieName":"刺杀盖世太保","onlineBoxRate":"--","splitSumBoxInfo":"189.8万"},{"showInfo":"1015","boxRate":"0.5%","releaseInfo":"上映5天","avgShowView":"22","sumBoxInfo":"549.0万","splitBoxRate":"0.5%","seatRate":"0.2%","movieId":343476,"splitBoxInfo":"64.52","avgSeatView":"19.9%","viewInfo":"2.1","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"68.66","avgViewBox":"31.8","splitAvgViewBox":"29.8","movieName":"不成问题的问题","onlineBoxRate":"--","splitSumBoxInfo":"512.9万"},{"showInfo":"1273","boxRate":"0.5%","releaseInfo":"上映57天","avgShowView":"16","sumBoxInfo":"22.03亿","splitBoxRate":"0.5%","seatRate":"0.3%","movieId":1198214,"splitBoxInfo":"61.67","avgSeatView":"17.0%","viewInfo":"2.0","showRate":"0.4%","releaseInfoColor":"#666666 1.00","boxInfo":"66.26","avgViewBox":"32.7","splitAvgViewBox":"30.5","movieName":"羞羞的铁拳","onlineBoxRate":"--","splitSumBoxInfo":"20.58亿"},{"showInfo":"1826","boxRate":"0.3%","releaseInfo":"上映9天","avgShowView":"7","sumBoxInfo":"3107.9万","splitBoxRate":"0.3%","seatRate":"0.4%","movieId":313,"splitBoxInfo":"34.01","avgSeatView":"9.6%","viewInfo":"1.1","showRate":"0.6%","releaseInfoColor":"#666666 1.00","boxInfo":"36.36","avgViewBox":"32.0","splitAvgViewBox":"29.9","movieName":"英雄本色","onlineBoxRate":"--","splitSumBoxInfo":"2890.0万"},{"showInfo":"768","boxRate":"0.2%","releaseInfo":"上映9天","avgShowView":"12","sumBoxInfo":"1992.0万","splitBoxRate":"0.2%","seatRate":"0.2%","movieId":248268,"splitBoxInfo":"27.15","avgSeatView":"13.0%","viewInfo":"0.9","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"29.00","avgViewBox":"31.9","splitAvgViewBox":"29.8","movieName":"恐袭波士顿","onlineBoxRate":"--","splitSumBoxInfo":"1847.4万"},{"showInfo":"52","boxRate":"0.2%","releaseInfo":"上映16天","avgShowView":"85","sumBoxInfo":"2722.4万","splitBoxRate":"0.2%","seatRate":"<0.1%","movieId":343985,"splitBoxInfo":"25.19","avgSeatView":"52.1%","viewInfo":"0.4","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"25.21","avgViewBox":"57.2","splitAvgViewBox":"57.2","movieName":"烽火芳菲","onlineBoxRate":"--","splitSumBoxInfo":"2673.0万"},{"showInfo":"3871","boxRate":"0.2%","releaseInfo":"上映2天","avgShowView":"3","sumBoxInfo":"118.7万","splitBoxRate":"0.2%","seatRate":"1.0%","movieId":1133164,"splitBoxInfo":"22.60","avgSeatView":"4.7%","viewInfo":"0.9","showRate":"1.4%","releaseInfoColor":"#666666 1.00","boxInfo":"24.23","avgViewBox":"26.0","splitAvgViewBox":"24.2","movieName":"七月半3：灵触第七感","onlineBoxRate":"--","splitSumBoxInfo":"110.6万"},{"showInfo":"142","boxRate":"0.1%","releaseInfo":"2014-08","avgShowView":"45","sumBoxInfo":"20.7万","splitBoxRate":"0.1%","seatRate":"<0.1%","movieId":246131,"splitBoxInfo":"19.08","avgSeatView":"7.2%","viewInfo":"0.6","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"20.67","avgViewBox":"32.5","splitAvgViewBox":"30.0","movieName":"泡菜","onlineBoxRate":"--","splitSumBoxInfo":"19.1万"},{"showInfo":"838","boxRate":"0.1%","releaseInfo":"上映9天","avgShowView":"7","sumBoxInfo":"2652.3万","splitBoxRate":"0.1%","seatRate":"0.2%","movieId":346511,"splitBoxInfo":"17.66","avgSeatView":"9.7%","viewInfo":"0.5","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"18.69","avgViewBox":"31.9","splitAvgViewBox":"30.1","movieName":"暴雪将至","onlineBoxRate":"--","splitSumBoxInfo":"2481.4万"},{"showInfo":"911","boxRate":"0.1%","releaseInfo":"上映15天","avgShowView":"8","sumBoxInfo":"2504.8万","splitBoxRate":"0.1%","seatRate":"0.2%","movieId":617007,"splitBoxInfo":"17.47","avgSeatView":"12.0%","viewInfo":"0.6","showRate":"0.3%","releaseInfoColor":"#666666 1.00","boxInfo":"18.28","avgViewBox":"27.1","splitAvgViewBox":"25.9","movieName":"精灵宝可梦：波尔凯尼恩与机巧的玛机雅娜","onlineBoxRate":"--","splitSumBoxInfo":"2319.1万"},{"showInfo":"175","boxRate":"<0.1%","releaseInfo":"上映23天","avgShowView":"20","sumBoxInfo":"1771.0万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":346641,"splitBoxInfo":"12.57","avgSeatView":"27.2%","viewInfo":"0.3","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"13.31","avgViewBox":"38.5","splitAvgViewBox":"36.3","movieName":"相爱相亲","onlineBoxRate":"--","splitSumBoxInfo":"1666.2万"},{"showInfo":"311","boxRate":"<0.1%","releaseInfo":"上映30天","avgShowView":"11","sumBoxInfo":"4.32亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":338402,"splitBoxInfo":"10.48","avgSeatView":"17.4%","viewInfo":"0.3","showRate":"0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"11.22","avgViewBox":"33.3","splitAvgViewBox":"31.1","movieName":"全球风暴","onlineBoxRate":"--","splitSumBoxInfo":"4.03亿"},{"showInfo":"421","boxRate":"<0.1%","releaseInfo":"上映43天","avgShowView":"10","sumBoxInfo":"5577.6万","splitBoxRate":"<0.1%","seatRate":"0.1%","movieId":880690,"splitBoxInfo":"9.23","avgSeatView":"15.6%","viewInfo":"0.3","showRate":"0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"9.83","avgViewBox":"25.3","splitAvgViewBox":"23.7","movieName":"我的爸爸是森林之王","onlineBoxRate":"--","splitSumBoxInfo":"5178.0万"},{"showInfo":"220","boxRate":"<0.1%","releaseInfo":"上映58天","avgShowView":"15","sumBoxInfo":"4.57亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1198177,"splitBoxInfo":"8.96","avgSeatView":"16.0%","viewInfo":"0.3","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"9.63","avgViewBox":"30.7","splitAvgViewBox":"28.6","movieName":"缝纫机乐队","onlineBoxRate":"--","splitSumBoxInfo":"4.29亿"},{"showInfo":"508","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"6","sumBoxInfo":"26.4万","splitBoxRate":"<0.1%","seatRate":"0.1%","movieId":246503,"splitBoxInfo":"8.47","avgSeatView":"13.8%","viewInfo":"0.2","showRate":"0.2%","releaseInfoColor":"#666666 1.00","boxInfo":"8.71","avgViewBox":"31.6","splitAvgViewBox":"30.7","movieName":"灿烂这一刻","onlineBoxRate":"--","splitSumBoxInfo":"25.7万"},{"showInfo":"219","boxRate":"<0.1%","releaseInfo":"上映16天","avgShowView":"7","sumBoxInfo":"2366.1万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":342193,"splitBoxInfo":"3.78","avgSeatView":"16.9%","viewInfo":"0.1","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"4.07","avgViewBox":"30.2","splitAvgViewBox":"28.1","movieName":"天生不对","onlineBoxRate":"--","splitSumBoxInfo":"2207.6万"},{"showInfo":"135","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"10","sumBoxInfo":"7.6万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1210973,"splitBoxInfo":"3.08","avgSeatView":"37.0%","viewInfo":"0.1","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"3.14","avgViewBox":"25.9","splitAvgViewBox":"25.3","movieName":"北纬二十八度","onlineBoxRate":"--","splitSumBoxInfo":"7.5万"},{"showInfo":"49","boxRate":"<0.1%","releaseInfo":"上映30天","avgShowView":"15","sumBoxInfo":"7688.3万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":247784,"splitBoxInfo":"2.85","avgSeatView":"22.3%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"3.06","avgViewBox":"44.6","splitAvgViewBox":"41.5","movieName":"银翼杀手2049","onlineBoxRate":"--","splitSumBoxInfo":"7241.1万"},{"showInfo":"282","boxRate":"<0.1%","releaseInfo":"上映9天","avgShowView":"4","sumBoxInfo":"343.9万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":635848,"splitBoxInfo":"2.44","avgSeatView":"6.8%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.62","avgViewBox":"27.2","splitAvgViewBox":"25.2","movieName":"深宫怨灵","onlineBoxRate":"--","splitSumBoxInfo":"316.7万"},{"showInfo":"353","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"4","sumBoxInfo":"11.9万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1211081,"splitBoxInfo":"2.40","avgSeatView":"28.8%","viewInfo":"0.1","showRate":"0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.60","avgViewBox":"21.7","splitAvgViewBox":"20.0","movieName":"炸裂青春","onlineBoxRate":"--","splitSumBoxInfo":"11.2万"},{"showInfo":"72","boxRate":"<0.1%","releaseInfo":"上映23天","avgShowView":"12","sumBoxInfo":"7917.7万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":338328,"splitBoxInfo":"2.51","avgSeatView":"18.3%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.58","avgViewBox":"30.2","splitAvgViewBox":"29.3","movieName":"密战","onlineBoxRate":"--","splitSumBoxInfo":"7398.8万"},{"showInfo":"14","boxRate":"<0.1%","releaseInfo":"上映9天","avgShowView":"55","sumBoxInfo":"14.7万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1207927,"splitBoxInfo":"2.27","avgSeatView":"33.2%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.46","avgViewBox":"32.5","splitAvgViewBox":"30.0","movieName":"一辈子的姐弟","onlineBoxRate":"--","splitSumBoxInfo":"14.4万"},{"showInfo":"67","boxRate":"<0.1%","releaseInfo":"上映57天","avgShowView":"21","sumBoxInfo":"5.75亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1176763,"splitBoxInfo":"1.99","avgSeatView":"19.9%","viewInfo":"0.1","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.24","avgViewBox":"16.7","splitAvgViewBox":"14.8","movieName":"追龙","onlineBoxRate":"--","splitSumBoxInfo":"5.37亿"},{"showInfo":"12","boxRate":"<0.1%","releaseInfo":"上映40天","avgShowView":"40","sumBoxInfo":"1128.7万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1208031,"splitBoxInfo":"2.21","avgSeatView":"14.4%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"2.21","avgViewBox":"46.2","splitAvgViewBox":"46.2","movieName":"你若安好","onlineBoxRate":"--","splitSumBoxInfo":"1128.1万"},{"showInfo":"4","boxRate":"<0.1%","releaseInfo":"2017-06","avgShowView":"129","sumBoxInfo":"7362.2万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1197625,"splitBoxInfo":"1.71","avgSeatView":"57.4%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.71","avgViewBox":"33.4","splitAvgViewBox":"33.4","movieName":"血战湘江","onlineBoxRate":"--","splitSumBoxInfo":"7343.0万"},{"showInfo":"8","boxRate":"<0.1%","releaseInfo":"点映","avgShowView":"61","sumBoxInfo":"6.0万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":672281,"splitBoxInfo":"1.45","avgSeatView":"77.8%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#FF9900 1.00","boxInfo":"1.57","avgViewBox":"32.5","splitAvgViewBox":"30.0","movieName":"这就是命","onlineBoxRate":"--","splitSumBoxInfo":"5.7万"},{"showInfo":"581","boxRate":"<0.1%","releaseInfo":"上映2天","avgShowView":"1","sumBoxInfo":"4.2万","splitBoxRate":"<0.1%","seatRate":"0.1%","movieId":337727,"splitBoxInfo":"1.46","avgSeatView":"8.5%","viewInfo":"0.0","showRate":"0.2%","releaseInfoColor":"#666666 1.00","boxInfo":"1.53","avgViewBox":"35.2","splitAvgViewBox":"33.7","movieName":"女巫斗恶龙","onlineBoxRate":"--","splitSumBoxInfo":"4.1万"},{"showInfo":"150","boxRate":"<0.1%","releaseInfo":"上映16天","avgShowView":"3","sumBoxInfo":"6741.9万","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":344500,"splitBoxInfo":"1.27","avgSeatView":"5.3%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.35","avgViewBox":"34.7","splitAvgViewBox":"32.9","movieName":"狂兽","onlineBoxRate":"--","splitSumBoxInfo":"6428.6万"},{"showInfo":"35","boxRate":"<0.1%","releaseInfo":"上映44天","avgShowView":"12","sumBoxInfo":"2.70亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1204596,"splitBoxInfo":"1.17","avgSeatView":"8.8%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.29","avgViewBox":"32.3","splitAvgViewBox":"29.3","movieName":"天才枪手","onlineBoxRate":"--","splitSumBoxInfo":"2.49亿"},{"showInfo":"10","boxRate":"<0.1%","releaseInfo":"上映44天","avgShowView":"48","sumBoxInfo":"1.06亿","splitBoxRate":"<0.1%","seatRate":"<0.1%","movieId":1209085,"splitBoxInfo":"1.23","avgSeatView":"69.6%","viewInfo":"0.0","showRate":"<0.1%","releaseInfoColor":"#666666 1.00","boxInfo":"1.23","avgViewBox":"26.3","splitAvgViewBox":"26.2","movieName":"十八洞村","onlineBoxRate":"--","splitSumBoxInfo":"1.06亿"}]
         * totalBoxUnit : 万
         * totalBox : 13751.3
         * splitTotalBoxUnit : 万
         * queryDate : 2017-11-25
         * serverTime : 2017-11-25 19:53:07
         * splitTotalBoxUnitInfo : 万
         * splitTotalBoxInfo : 12941.8
         */

        private String updateInfo;
        private String totalBoxUnitInfo;
        private String splitTotalBox;
        private long serverTimestamp;
        private CrystalBean crystal;
        private String totalBoxInfo;
        private String totalBoxUnit;
        private String totalBox;
        private String splitTotalBoxUnit;
        private String queryDate;
        private String serverTime;
        private String splitTotalBoxUnitInfo;
        private String splitTotalBoxInfo;
        private List<ListBean> list;

        public String getUpdateInfo() {
            return updateInfo;
        }

        public void setUpdateInfo(String updateInfo) {
            this.updateInfo = updateInfo;
        }

        public String getTotalBoxUnitInfo() {
            return totalBoxUnitInfo;
        }

        public void setTotalBoxUnitInfo(String totalBoxUnitInfo) {
            this.totalBoxUnitInfo = totalBoxUnitInfo;
        }

        public String getSplitTotalBox() {
            return splitTotalBox;
        }

        public void setSplitTotalBox(String splitTotalBox) {
            this.splitTotalBox = splitTotalBox;
        }

        public long getServerTimestamp() {
            return serverTimestamp;
        }

        public void setServerTimestamp(long serverTimestamp) {
            this.serverTimestamp = serverTimestamp;
        }

        public CrystalBean getCrystal() {
            return crystal;
        }

        public void setCrystal(CrystalBean crystal) {
            this.crystal = crystal;
        }

        public String getTotalBoxInfo() {
            return totalBoxInfo;
        }

        public void setTotalBoxInfo(String totalBoxInfo) {
            this.totalBoxInfo = totalBoxInfo;
        }

        public String getTotalBoxUnit() {
            return totalBoxUnit;
        }

        public void setTotalBoxUnit(String totalBoxUnit) {
            this.totalBoxUnit = totalBoxUnit;
        }

        public String getTotalBox() {
            return totalBox;
        }

        public void setTotalBox(String totalBox) {
            this.totalBox = totalBox;
        }

        public String getSplitTotalBoxUnit() {
            return splitTotalBoxUnit;
        }

        public void setSplitTotalBoxUnit(String splitTotalBoxUnit) {
            this.splitTotalBoxUnit = splitTotalBoxUnit;
        }

        public String getQueryDate() {
            return queryDate;
        }

        public void setQueryDate(String queryDate) {
            this.queryDate = queryDate;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

        public String getSplitTotalBoxUnitInfo() {
            return splitTotalBoxUnitInfo;
        }

        public void setSplitTotalBoxUnitInfo(String splitTotalBoxUnitInfo) {
            this.splitTotalBoxUnitInfo = splitTotalBoxUnitInfo;
        }

        public String getSplitTotalBoxInfo() {
            return splitTotalBoxInfo;
        }

        public void setSplitTotalBoxInfo(String splitTotalBoxInfo) {
            this.splitTotalBoxInfo = splitTotalBoxInfo;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class CrystalBean {
            /**
             * status : 0
             */

            private int status;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class ListBean {
            /**
             * showInfo : 43687
             * boxRate : 30.7%
             * releaseInfo : 上映2天
             * avgShowView : 29
             * sumBoxInfo : 5500.4万
             * splitBoxRate : 30.4%
             * seatRate : 14.7%
             * movieId : 342068
             * splitBoxInfo : 3941.77
             * avgSeatView : 24.7%
             * viewInfo : 122.6
             * showRate : 15.3%
             * releaseInfoColor : #666666 1.00
             * boxInfo : 4222.54
             * avgViewBox : 34.4
             * splitAvgViewBox : 32.2
             * movieName : 寻梦环游记
             * onlineBoxRate : --
             * splitSumBoxInfo : 5134.8万
             * milestone : {"box":50000000000,"boxCopy":"破5亿","dateTime":1511601700000,"imageUrl":"https://img.meituan.net/pro/73b8973160be5fd60982305ec73b07ad420362.jpg","movieId":341195}
             */

            private String showInfo;
            private String boxRate;
            private String releaseInfo;
            private String avgShowView;
            private String sumBoxInfo;
            private String splitBoxRate;
            private String seatRate;
            private int movieId;
            private String splitBoxInfo;
            private String avgSeatView;
            private String viewInfo;
            private String showRate;
            private String releaseInfoColor;
            private String boxInfo;
            private String avgViewBox;
            private String splitAvgViewBox;
            private String movieName;
            private String onlineBoxRate;
            private String splitSumBoxInfo;
            private MilestoneBean milestone;

            public String getShowInfo() {
                return showInfo;
            }

            public void setShowInfo(String showInfo) {
                this.showInfo = showInfo;
            }

            public String getBoxRate() {
                return boxRate;
            }

            public void setBoxRate(String boxRate) {
                this.boxRate = boxRate;
            }

            public String getReleaseInfo() {
                return releaseInfo;
            }

            public void setReleaseInfo(String releaseInfo) {
                this.releaseInfo = releaseInfo;
            }

            public String getAvgShowView() {
                return avgShowView;
            }

            public void setAvgShowView(String avgShowView) {
                this.avgShowView = avgShowView;
            }

            public String getSumBoxInfo() {
                return sumBoxInfo;
            }

            public void setSumBoxInfo(String sumBoxInfo) {
                this.sumBoxInfo = sumBoxInfo;
            }

            public String getSplitBoxRate() {
                return splitBoxRate;
            }

            public void setSplitBoxRate(String splitBoxRate) {
                this.splitBoxRate = splitBoxRate;
            }

            public String getSeatRate() {
                return seatRate;
            }

            public void setSeatRate(String seatRate) {
                this.seatRate = seatRate;
            }

            public int getMovieId() {
                return movieId;
            }

            public void setMovieId(int movieId) {
                this.movieId = movieId;
            }

            public String getSplitBoxInfo() {
                return splitBoxInfo;
            }

            public void setSplitBoxInfo(String splitBoxInfo) {
                this.splitBoxInfo = splitBoxInfo;
            }

            public String getAvgSeatView() {
                return avgSeatView;
            }

            public void setAvgSeatView(String avgSeatView) {
                this.avgSeatView = avgSeatView;
            }

            public String getViewInfo() {
                return viewInfo;
            }

            public void setViewInfo(String viewInfo) {
                this.viewInfo = viewInfo;
            }

            public String getShowRate() {
                return showRate;
            }

            public void setShowRate(String showRate) {
                this.showRate = showRate;
            }

            public String getReleaseInfoColor() {
                return releaseInfoColor;
            }

            public void setReleaseInfoColor(String releaseInfoColor) {
                this.releaseInfoColor = releaseInfoColor;
            }

            public String getBoxInfo() {
                return boxInfo;
            }

            public void setBoxInfo(String boxInfo) {
                this.boxInfo = boxInfo;
            }

            public String getAvgViewBox() {
                return avgViewBox;
            }

            public void setAvgViewBox(String avgViewBox) {
                this.avgViewBox = avgViewBox;
            }

            public String getSplitAvgViewBox() {
                return splitAvgViewBox;
            }

            public void setSplitAvgViewBox(String splitAvgViewBox) {
                this.splitAvgViewBox = splitAvgViewBox;
            }

            public String getMovieName() {
                return movieName;
            }

            public void setMovieName(String movieName) {
                this.movieName = movieName;
            }

            public String getOnlineBoxRate() {
                return onlineBoxRate;
            }

            public void setOnlineBoxRate(String onlineBoxRate) {
                this.onlineBoxRate = onlineBoxRate;
            }

            public String getSplitSumBoxInfo() {
                return splitSumBoxInfo;
            }

            public void setSplitSumBoxInfo(String splitSumBoxInfo) {
                this.splitSumBoxInfo = splitSumBoxInfo;
            }

            public MilestoneBean getMilestone() {
                return milestone;
            }

            public void setMilestone(MilestoneBean milestone) {
                this.milestone = milestone;
            }

            public static class MilestoneBean {
                /**
                 * box : 50000000000
                 * boxCopy : 破5亿
                 * dateTime : 1511601700000
                 * imageUrl : https://img.meituan.net/pro/73b8973160be5fd60982305ec73b07ad420362.jpg
                 * movieId : 341195
                 */

                private long box;
                private String boxCopy;
                private long dateTime;
                private String imageUrl;
                private int movieId;

                public long getBox() {
                    return box;
                }

                public void setBox(long box) {
                    this.box = box;
                }

                public String getBoxCopy() {
                    return boxCopy;
                }

                public void setBoxCopy(String boxCopy) {
                    this.boxCopy = boxCopy;
                }

                public long getDateTime() {
                    return dateTime;
                }

                public void setDateTime(long dateTime) {
                    this.dateTime = dateTime;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public int getMovieId() {
                    return movieId;
                }

                public void setMovieId(int movieId) {
                    this.movieId = movieId;
                }
            }
        }
    }
}

