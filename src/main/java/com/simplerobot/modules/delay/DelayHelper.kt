package com.simplerobot.modules.delay

import com.simplerobot.modules.delay.DelayHelper.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * 延时工具类
 */
object DelayHelper {
    @JvmStatic
    val instance = DelayHelper

    /**
     * 延迟执行一个异步任务
     * @param task 任务函数
     * @param time 延迟时间
     */
    inline fun launch(crossinline task: () -> Unit, time: Long) =
            GlobalScope.launch {
                delay(time)
                task()
            }


    /**
     * 延迟执行一个异步任务
     * @param task 任务函数
     * @param time 延迟时间
     * @param timeUnit 时间类型，默认为秒
     */
    inline fun delayTask(crossinline task: () -> Unit, time: Long, timeUnit: TimeUnit = TimeUnit.SECONDS) = launch(task, timeUnit.toMillis(time))


    /**
     * 延迟执行一个异步任务
     * @param task 任务函数
     * @param time 延迟时间
     * @param timeUnit 时间类型，默认为秒
     */
    inline fun delayTask(crossinline task: () -> Unit, time: Int, timeUnit: TimeUnit = TimeUnit.SECONDS) = delayTask(task, time.toLong(), timeUnit)


    /**
     * 接收一个参数
     */
    @JvmOverloads
    fun delayTask(task: Runnable, time: Long, timeUnit: TimeUnit = TimeUnit.SECONDS) = delayTask(task::run, time, timeUnit)


    @JvmOverloads
    fun delayTask(task: Runnable, time: Int, timeUnit: TimeUnit = TimeUnit.SECONDS) = delayTask(task, time.toLong(), timeUnit)

}


/**
 * 时间修改
 */
sealed class TimeMill(var delayTime: Long) {
    protected val mill get() = delayTime
    // 根据时间格式设置时间
    fun setDelayTime(t: Long, timeUnit: TimeUnit) {
        delayTime = timeUnit.toMillis(t)
    }
}

/**
 * 延时送信器，通过一个真实的送信器实现
 * 需要注意的是，最终的延迟任务的返回值都将会是 null/true
 */
class DelaySender
@JvmOverloads
constructor(private val sender: com.forte.qqrobot.sender.senderlist.SenderSendList,
            time: Long,
            timeUnit: TimeUnit = TimeUnit.SECONDS) :
        TimeMill(timeUnit.toMillis(time)),
        com.forte.qqrobot.sender.senderlist.SenderSendList {
    @JvmOverloads
    constructor(sender: com.forte.qqrobot.sender.senderlist.SenderSendList, time: Int, timeUnit: TimeUnit = TimeUnit.SECONDS) : this(sender, time.toLong(), timeUnit)

    /**
     * 发送讨论组消息
     * @param group 群号
     * @param msg   消息内容
     */
    override fun sendDiscussMsg(group: String?, msg: String?): String? {
        launch({ sender.sendDiscussMsg(group, msg) }, mill)
        return null
    }


    /**
     * 发布群公告
     * 目前，top、toNewMember、confirm参数是无效的
     * @param group 群号
     * @param title 标题
     * @param text   正文
     * @param top    是否置顶，默认false
     * @param toNewMember 是否发给新成员 默认false
     * @param confirm 是否需要确认 默认false
     * @return 是否发布成功
     */
    override fun sendGroupNotice(group: String?, title: String?, text: String?, top: Boolean, toNewMember: Boolean, confirm: Boolean): Boolean {
        launch({ sender.sendGroupNotice(group, title, text, top, toNewMember, confirm) }, mill)
        return true
    }

    /**
     * 发送群消息
     * @param group 群号
     * @param msg   消息内容
     */
    override fun sendGroupMsg(group: String?, msg: String?): String? {
        launch({ sender.sendGroupMsg(group, msg) }, mill)
        return null
    }

    /**
     * 发送私聊信息
     * @param QQ    QQ号
     * @param msg   消息内容
     */
    override fun sendPrivateMsg(QQ: String?, msg: String?): String? {
        launch({ sender.sendPrivateMsg(QQ, msg) }, mill)
        return null
    }

    /**
     * 发送名片赞
     * @param QQ    QQ号
     * @param times 次数
     */
    override fun sendLike(QQ: String?, times: Int): Boolean {
        launch({ sender.sendLike(QQ, times) }, mill)
        return true
    }

    /**
     * 送花
     * @param group 群号
     * @param QQ    QQ号
     */
    override fun sendFlower(group: String?, QQ: String?): Boolean {
        launch({ sender.sendFlower(group, QQ) }, mill)
        return true
    }
}

/**
 * 延时置信器，通过一个真实的置信器实现
 * 需要注意的是，最终的延迟任务的返回值都将会是 null/true
 */
class DelaySetter
@JvmOverloads
constructor(private val setter: com.forte.qqrobot.sender.senderlist.SenderSetList,
            time: Long,
            timeUnit: TimeUnit = TimeUnit.SECONDS) :
        TimeMill(timeUnit.toMillis(time)),
        com.forte.qqrobot.sender.senderlist.SenderSetList {
    @JvmOverloads
    constructor(setter: com.forte.qqrobot.sender.senderlist.SenderSetList, time: Int, timeUnit: TimeUnit = TimeUnit.SECONDS) : this(setter, time.toLong(), timeUnit)

    /**
     * 设置匿名成员禁言
     * @param group 群号
     * @param flag  匿名成员标识
     * @param time  时长，一般是以分钟为单位
     */
    override fun setGroupAnonymousBan(group: String?, flag: String?, time: Long): Boolean {
        launch({ setter.setGroupAnonymousBan(group, flag, time) }, mill)
        return true
    }

    /**
     * 踢出群成员
     * @param group 群号
     * @param QQ    QQ号
     * @param dontBack  是否拒绝再次申请
     */
    override fun setGroupMemberKick(group: String?, QQ: String?, dontBack: Boolean): Boolean {
        launch({ setter.setGroupMemberKick(group, QQ, dontBack) }, mill)
        return true
    }

    /**
     * 退出讨论组
     * @param group 讨论组号
     */
    override fun setDiscussLeave(group: String?): Boolean {
        launch({ setter.setDiscussLeave(group) }, mill)
        return true
    }

    /**
     * 设置群管理员
     * @param group 群号
     * @param QQ    qq号
     * @param set   是否设置为管理员
     */
    override fun setGroupAdmin(group: String?, QQ: String?, set: Boolean): Boolean {
        launch({ setter.setGroupAdmin(group, QQ, set) }, mill)
        return true
    }

    /**
     * 是否允许群匿名聊天
     * @param group 群号
     * @param agree 是否允许
     */
    override fun setGroupAnonymous(group: String?, agree: Boolean): Boolean {
        launch({ setter.setGroupAnonymous(group, agree) }, mill)
        return true
    }

    /**
     * 群签到
     * @param group 群号
     */
    override fun setGroupSign(group: String?): Boolean {
        launch({ setter.setGroupSign(group) }, mill)
        return true
    }

    /**
     * 打卡
     */
    override fun setSign(): Boolean {
        launch({ setter.setSign() }, mill)
        return true
    }

    /**
     * 好友请求申请
     * @param flag  一般会有个标识
     * @param friendName    如果通过，则此参数为好友备注
     * @param agree 是否通过
     */
    override fun setFriendAddRequest(flag: String?, friendName: String?, agree: Boolean): Boolean {
        launch({ setter.setFriendAddRequest(flag, friendName, agree) }, mill)
        return true
    }

    /**
     * 删除群文件<br></br>
     * ! 此接口可能不成熟
     * @param group 群号
     * @param flag  一般应该会有个标识
     */
    override fun setGroupFileDelete(group: String?, flag: String?): Boolean {
        launch({ setter.setGroupFileDelete(group, flag) }, mill)
        return true
    }

    /**
     * 消息撤回 似乎只需要一个消息ID即可
     * 需要pro
     * @param flag  消息标识
     */
    override fun setMsgRecall(flag: String?): Boolean {
        launch({ setter.setMsgRecall(flag) }, mill)
        return true
    }

    /**
     * 设置群成员名片
     * @param group 群号
     * @param QQ    QQ号
     * @param card  名片
     */
    override fun setGroupCard(group: String?, QQ: String?, card: String?): Boolean {
        launch({ setter.setGroupCard(group, QQ, card) }, mill)
        return true
    }

    /**
     * 设置全群禁言
     * @param group 群号
     * @param in    是否开启全群禁言
     */
    override fun setGroupWholeBan(group: String?, `in`: Boolean): Boolean {
        launch({ setter.setGroupWholeBan(group, `in`) }, mill)
        return true
    }

    /**
     * 群添加申请
     * @param flag  一般会有个标识
     * @param requestType   加群类型  邀请/普通添加
     * @param agree 是否同意
     * @param why   如果拒绝，则此处为拒绝理由
     */
    override fun setGroupAddRequest(flag: String?, requestType: com.forte.qqrobot.beans.messages.types.GroupAddRequestType?, agree: Boolean, why: String?): Boolean {
        launch({ setter.setGroupAddRequest(flag, requestType, agree, why) }, mill)
        return true
    }

    /**
     * 退出群
     * @param group 群号
     * @param dissolve 假如此账号是群主，则此参数代表是否要解散群。默认为false
     */
    override fun setGroupLeave(group: String?, dissolve: Boolean): Boolean {
        launch({ setter.setGroupLeave(group, dissolve) }, mill)
        return true
    }

    /**
     * 设置群成员专属头衔
     * @param group 群号
     * @param QQ    QQ号
     * @param title 头衔
     * @param time  有效时长，一般为分钟吧
     */
    override fun setGroupExclusiveTitle(group: String?, QQ: String?, title: String?, time: Long): Boolean {
        launch({ setter.setGroupExclusiveTitle(group, QQ, title, time) }, mill)
        return true
    }

    /**
     * 设置群禁言
     * @param group 群号
     * @param QQ    QQ号
     * @param time  时长，一般是以秒为单位
     */
    override fun setGroupBan(group: String?, QQ: String?, time: Long): Boolean {
        launch({ setter.setGroupBan(group, QQ, time) }, mill)
        return true
    }


}