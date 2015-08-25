#1. 外部API可行性分析

##新浪微博
>注：新浪微博每个用户有UID，每条微博有MID，以下API有些API支持批量获取使用UID，只需要在相应URL后加入\uid即可

###获取微博转发量、评论数、赞数
1. 单条
http://open.weibo.com/wiki/2/statuses/show
2. 批量
http://open.weibo.com/wiki/2/statuses/count

###获取用户的微博量，判断是否水军
1. 批量
http://open.weibo.com/wiki/2/users/counts

###获取微博的转发历史，用于转发链
1. 单条
http://open.weibo.com/wiki/2/statuses/repost_timeline

###基于性别的分析等 基于时间的分析 基于地理位置的分析 基于关键词(#annotations)的数据分析
1. 根据微博ID返回某条微博的评论列表（包括性别、时间、地理位置、关键词）
http://open.weibo.com/wiki/2/comments/show

2. 根据转发历史中的转发用户统计（包括性别、时间、地理位置、关键词）
http://open.weibo.com/wiki/2/statuses/repost_timeline

### 基于好友的分析
1. 获取用户关注列表
http://open.weibo.com/wiki/2/friendships/friends
2. 获取用户粉丝列表
http://open.weibo.com/wiki/2/friendships/followers
3. 获取活跃粉丝列表
http://open.weibo.com/wiki/2/friendships/followers/active
4. 获取互相关注的列表
http://open.weibo.com/wiki/2/friendships/friends/bilateral
5. 获取双向关注用户的最新微博
http://open.weibo.com/wiki/2/statuses/bilateral_timeline

###基于兴趣的分析（难点）
1. 获取互相关注的列表
http://open.weibo.com/wiki/2/friendships/friends/bilateral
2. 获取收藏列表
http://open.weibo.com/wiki/2/favorites

###基于热点分析
1. 返回最新的公共微博
http://open.weibo.com/wiki/2/statuses/public_timeline

###用于交互页面和体验（不必要）
1. 返回用户自己及关注者最新微博
http://open.weibo.com/wiki/2/statuses/friends_timeline

2. 跳转链接到相应微博
http://open.weibo.com/wiki/2/statuses/go

##人人
>注：API很少，转发链没有API，考虑爬虫？人人独有资源概念（ugc），没有关键词

###获取状态转发、评论数
1. http://open.renren.com/wiki/V2/status/get

###获取资源被点赞次数
1. http://open.renren.com/wiki/V2/like/ugc/info/get

###获取用户的状态、评论量，判断水军
1. http://open.renren.com/wiki/Profile

###基于好友的分析
1. 获取某个用户的好友ID列表
http://open.renren.com/wiki/V2/friend/list
2. 以分页的方式获取某个用户与当前登录用户的共同好友
http://open.renren.com/wiki/V2/user/friend/mutual/list
3. 批量获取用户信息
http://open.renren.com/wiki/V2/user/batch

###基于时间的分析
1. 以分页的方式获取某个用户的分享列表
http://open.renren.com/wiki/V2/share/list
2. 获取用户状态
http://open.renren.com/wiki/V2/status/get
3. 获取用户状态列表
http://open.renren.com/wiki/V2/status/list
4. 以分页的方式获取某个UGC的评论
http://open.renren.com/wiki/V2/comment/list

###基于地理位置的分析
1. 获取自己和好友的带lbs信息的feed列表
http://open.renren.com/wiki/V2/place/friend/feed/list

###基于性别的分析
1. 以分页的方式获取某个UGC的评论
http://open.renren.com/wiki/V2/comment/list

###基于热点分析
1. 返回指定经纬度热门新鲜事
http://open.renren.com/wiki/V2/location/feed/list
2. 通过地点获取新鲜事
http://open.renren.com/wiki/V2/place/feed/list

###用户交互页面和体验（不必要）
1. 返回用户自己的状态列表
http://open.renren.com/wiki/V2/status/list

#2. 内部API设计

