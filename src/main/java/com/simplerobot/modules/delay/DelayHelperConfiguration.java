package com.simplerobot.modules.delay;

import com.forte.qqrobot.anno.depend.Beans;

/**
 * 将DelayHelper的单例实例装配到依赖中心
 */
@Beans
public class DelayHelperConfiguration {

    @Beans("delayHelper")
    public DelayHelper getDelayHelper(){
        return DelayHelper.getInstance();
    }

}
