package com.example.administrator.txtread;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TxtUtil {

    private static String mData = "\n" +
            "\t\t\t\t&lt;&gt;一秒记住【爱去小说网.】，为您提供精彩小说阅读。<br/>　　<br/>　　龙山历9616年，冬。<br/>　　<br/>　　安阳行省，青河郡，仪水县城境内。<br/>　　<br" +
            "/>　　一名穿着剪裁精致的白色毛皮衣的唇红齿白的约莫八九岁的男孩，背着一矛囊，正灵活的飞窜在山林间，右手也持着一根黑色木柄短矛，追逐着前方的一头仓皇逃窜的野鹿，周围的树叶震动积雪簌簌而落。<br/>　　<br/>　　“着！”<br/>　　<br/>　　飞窜中的男孩猛然高举短矛，身体微微往后仰，腰腹力量传递到右臂，猛然一甩！<br/>　　<br/>　　刷！<br" +
            "/>　　<br/>　　手中短矛破空飞出，擦着一些树叶，穿过三十余米距离，从野鹿背部边缘一擦而过，而后扎入雪地深处，仅仅在野鹿背部留下一道血痕，野鹿顿时更加拼命跑，朝山林深处钻去，眼看着就要跑丢。<br/>　　<br/>　　忽然嗖的一声，一颗石头飞出。<br/>　　<br" +
            "/>　　石头化作流光穿行在山林间，飞过上百米距离，砰的声，贯穿了一株大树的树干，精准的射入了那头野鹿的头颅内，那野鹿坚硬的头骨也抵挡不住，踉跄着靠着惯性飞奔出十余米便轰然倒地，震的周围的无数积雪簌簌而落。<br/>　　<br/>　　“父亲。”男孩转头看向远处，有些无奈道，“你别出手啊，我差点就能射中它了。”<br/>　　<br" +
            "/>　　“我不出手，那野鹿就跑没了。高速飞奔中你的短矛准头还差些，今天傍晚回去加练五百次短矛。”声音雄浑，远远传来，远处两道身影正并肩走来。<br/>　　<br" +
            "/>　　一名是颇为壮硕的黑发黑眼中年男子，身后背负着一兵器箱。另外一道身影却是更加魁梧壮硕，高过两米，手臂比常人大腿还粗，可他却有着狮子般的脑袋，正是狮首人躯！凌乱的黄色头发披散着，这赫然便是颇为少见的兽人中的‘狮人’，他同样背着一兵器箱。<br/>　　<br/>　　“铜三老弟，你看我儿子厉害吧，今年才八岁，已经有寻常成年男子的力气了。”中年男子笑道。<br" +
            "/>　　<br/>　　“嗯，雪鹰是不错，将来比你强是没问题的。”狮人壮汉打趣道。<br/>　　<br/>　　“当然比我厉害，我八岁的时候还穷哈哈的和村里小孩玩闹，啥都不懂呢，还是后来进入军队才有机会修炼斗气！”中年男子感慨道，“我这个当父亲的，给不了儿子太过好的条件，不过能给的，我都会倾力给他，好好栽培他。”<br/>　　<br" +
            "/>　　“东伯，你能从一个平民，成为一名天阶骑士，更能买下领地成为一名贵族，已经很厉害了。”狮人壮汉笑道。<br/>　　<br/>　　这中年男子正是周围过百里领地的领主——男爵东伯烈！<br/>　　<br" +
            "/>　　男爵是夏族帝国‘龙山帝国’最低的一个贵族爵位，在帝国建国时，贵族爵位授予还很严格，如今帝国建立至今已经九千多年，这个庞然大物开始腐朽，一些低爵位买卖甚至都是官方允许。<br/>　　<br/>　　当初东伯烈和妻子因为有了孩子，才决定买下贵族爵位，买下一块领地，这块领地更是起名为——雪鹰领！和他们的儿子同名，可见对这儿子的疼爱。【爱去小说网" +
            ".】<br/>　　<br/>　　当然这仅仅只是仪水县内的一块小领地。<br/>　　<br/>　　“我二十岁才修炼出斗气，可我儿子不同，他今年才八岁，我估摸着十岁左右就能修炼出斗气，哈哈，肯定比我强多了。”东伯烈看着那男孩，眼中满是父亲对儿子的宠溺和期待。<br/>　　<br/>　　“看他的力气，十岁左右是差不多了。”狮人壮汉也赞同。<br/>　　<br" +
            "/>　　他们经历的太多太多了，眼光自然很准。<br/>　　<br/>　　“父亲，你在那么远，扔的石头都能贯穿这么粗的大树？”男孩正站在那一株大树旁，双手去抱，竟然都无法完全抱住，这大树的树干上却被贯穿出一个大窟窿，“这么粗的大树啊，让我慢慢砍，都要砍上好久好久。”<br/>　　<br" +
            "/>　　“知道天阶骑士的厉害了吧。”狮人壮汉说道，旁边东伯烈也得意一笑，在儿子面前当父亲的还是喜欢显摆显摆的。<br/>　　<br/>　　“有神厉害吗？”男孩故意撇嘴。<br/>　　<br/>　　“神？”<br/>　　<br/>　　东伯烈、狮人铜三顿时无语。<br/>　　<br" +
            "/>　　龙山帝国的开创者‘龙山大帝’就是一位强大的神灵，这是这个世界几乎所有子民都知道的，东伯烈在军队中也算一员猛将了，可和神灵相比？根本没法比啊。<br/>　　<br/>　　“看来今天傍晚加练五百次短矛，还是少了，嗯，就加练一千次吧。”东伯烈砸着嘴巴说道。<br/>　　<br/>　　“父亲！”男孩瞪大眼睛，“你，你……”<br/>　　<br" +
            "/>　　“看你还敢跟我拌嘴，要记住，和你父亲拌嘴，你只会吃亏，好了，回了回了。”东伯烈说道。<br/>　　<br/>　　狮人壮汉‘铜三’从脖子中取出一乌笛，放在嘴边吹出了低沉的声音，声音在山林间传播。<br/>　　<br/>　　很快远处有二十名穿着甲铠的士兵迅速赶来。<br/>　　<br/>　　“将猎物带回去。”东伯烈吩咐道。<br/>　　<br" +
            "/>　　“是，领主大人。”士兵们都恭敬应命。<br/>　　<br/>　　东伯烈、狮人壮汉带着男孩雪鹰走到了这座山的最高处，这里正有着些大量的马匹以及近百名士兵，一片空旷的雪地上正铺着巨大的白色毛毯，毛毯上正有坐着一名气息神秘超然的紫袍女子，紫袍女子的身边是一名可爱的奔跑还踉踉跄跄的两三岁孩童，那些士兵们看向紫袍女子的目光中有着敬畏。<br/>　　<br" +
            "/>　　因为这紫袍女子是一名强大的法师！<br/>　　<br/>　　“石头，快看，谁来了。”紫袍女子笑着说道，那两三岁孩童立即转头看去，一看就眼睛亮了。<br/>　　<br/>　　“哥哥抱，哥哥抱。”孩童扭着屁股飞奔过去。<br/>　　<br/>　　紫袍女子也微笑看着这一幕。<br/>　　<br" +
            "/>　　“石头。”男孩雪鹰立即走到了最前面蹲下，弟弟青石飞扑到他的怀里：“哥哥抱，哥哥抱。”<br/>　　<br/>　　雪鹰抱起了弟弟，亲了下弟弟。<br/>　　<br/>　　“石头，今天猎到一头野鹿哦，你看。”雪鹰指着后面士兵抬着的野鹿。<br/>　　<br/>　　“夜炉？夜炉？”弟弟青石瞪大着乌溜溜的眼睛，嘴里发出不清晰的声音。<br/>　　<br" +
            "/>　　弟弟‘东伯青石’才两岁，虽然努力说话，可说话还不够清晰，也不太懂意思。<br/>　　<br/>　　“是野鹿，我们家后山中的一种野兽。”雪鹰说道。<br/>　　<br/>　　“雪鹰，把弟弟给我吧。”紫袍女子也起身走来。<br/>　　<br/>　　“是，母亲。”雪鹰将弟弟递过去。<br/>　　<br" +
            "/>　　紫袍女子说道：“我带了些桂花糕点还热着，就在篮子里，赶紧去吃吧。”<br/>　　<br/>　　“糕点？”雪鹰眼睛一亮一时间口水分泌，感觉口水都快流出来了，立即飞奔着过去。<br/>　　<br/>　　“我也要吃，我也要吃。”弟弟青石立即在母亲怀里挣扎了，提到‘吃糕点’他才是最积极的，平常吃饭反而很不听话。<br/>　　<br" +
            "/>　　“当然有你的，你这个小馋嘴。”紫袍女子看了外面也走进来的东伯烈和狮人铜三，“你们俩也快点，也给你准备了些吃的。”<br/>　　<br/>　　“哈哈……主人不但法术厉害，这厨艺也好啊。”狮人壮汉说道。<br/>　　<br/>　　这狮人在年少时曾是奴隶，成为了紫袍女子的仆从追随，虽然多年过去，彼此感情也仿佛亲人，可狮人壮汉依旧坚持喊‘主人’。<br/>　　<br" +
            "/>　　……<br/>　　<br/>　　雪鹰吃饱喝足后朝远处看去，因为他们露营的地方就是在山顶，一眼看去，远处也有一些山，也有许多农地，目光所至，都是自家的领地。父亲和母亲当年就是因为自己的出生，才停止了冒险的日子，买下贵族爵位，买下了一大片领地，这片领地都被起名为——雪鹰领！<br/>　　<br/>　　东伯雪鹰伸了个懒腰，满脸开心。<br/>　　<br" +
            "/>　　有疼爱自己的父亲母亲，有可爱的弟弟，有许多善意的领地子民。<br/>　　<br/>　　对这样的日子，东伯雪鹰真的太满意了……<br/>　　<br/>　　唯一让他有些头疼的，就是父亲的训练有些太苦了。<br/>　　<br/>　　“要加练一千次短矛，加上原有的一千次……还有更主要的枪法，还有……”东伯雪鹰的小脸都成苦瓜脸了。<br/>　　<br" +
            "/>　　******<br/>　　<br/>　　夜幕降临，残月悬空。<br/>　　<br/>　　风在呼啸。<br/>　　<br/>　　“轰~~~”<br/>　　<br/>　　在距离地面数千米的高空中，仿佛一片乌云的巨大的鸟在高速飞行着。<br/>　　<br" +
            "/>　　这一头巨大的鸟，翼展过二十米，有着足足四个翅膀，它飞行的速度达到了近音速的地步，正是一头极凶戾的可怕魔兽‘四翼秃鹫’，在这头四翼秃鹫背上正盘膝坐着两道身影，一名银甲男子，以及一名持着暗紫色木杖的灰袍人。<br/>　　<br/>　　“到哪了？”灰袍人问道。<br/>　　<br" +
            "/>　　“禀主人，已经进入仪水县境内，估计还有半个时辰就能抵达雪鹰领。”银甲男子俯瞰下方，目光冰冷，清晰辨别下方位置。<br/>　　<br/>　　“还有半个时辰，我就能看到我那位妹妹了。”灰袍人声音很复杂，“真的很能躲啊，在我们家族的追查下，躲了足足十五年……”<br/>　　<br/>　　四翼秃鹫在黑夜中，直奔雪鹰领！<br/>　　<br/>　　——————<br" +
            "/>　　<br/>　　番茄新书正式发布！<br/>　　<br/>　　呼，又要开始一个崭新的世界了，还请大家记得收藏、推荐啊~~~<br/>　　<br/>　　**<br/>　　<br/>　　手机用户请浏览w阅读，更优质的阅读体验。\n";

    private static Paint mPaint;

    private static int mX = 5;

    private static int mFontHeight = -1;

    private static int mY = 80;

    // 行距
    private static int mHangju = 0;

    // 字体大小
    private static int mTextSize = 50;

    // 字体颜色
    private static int textColor= Color.RED;

    // 背景颜色
    private static int bColor = Color.WHITE;

    private static List<String> mLines = new ArrayList<>();

    /**
     * 获取格式化后的数据
     *
     * @return
     */
    public static String getData() {
        mData = mData.replace("<br/>", "\n").replace("&lt;", "").replace("&gt;", "");
        return mData;
    }

    public static void setData(String str) {
        mData = str;
        // 章节数据发生变化,分页数据要刷新
        mLines.clear();
    }

    /**
     * 获取文字显示的高度
     *
     * @return
     */
    public static int getFontHeight() {
        if (mFontHeight == -1) {
            Paint.FontMetrics fm = getTxtPaint().getFontMetrics();
            return (int) Math.ceil(fm.descent - fm.top) + 2;
        } else {
            return mFontHeight;
        }
    }

    /**
     * 获取paint对象
     *
     * @return
     */
    public static Paint getTxtPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(textColor);
            mPaint.setTypeface(Typeface.DEFAULT);
            //paint.setAntiAlias(true);
            mPaint.setFilterBitmap(true);
            mPaint.setSubpixelText(true);
            mPaint.setTextSize(mTextSize);
        }
        return mPaint;
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    public static void setTxtPaintTextSize(int size) {
        getTxtPaint().setTextSize(size);
        mTextSize = size;
        mY = size + 30;
        // 字体大小变化后,分页数据要刷新,字体高度要刷新
        mFontHeight = -1;
        mLines.clear();
    }

    public static void setTextColor(int color){
        getTxtPaint().setColor(color);
        textColor = color;
    }

    public static void setbColor(int color){
        bColor = color;
    }

    /**
     * 设置行距大小
     * @param hangju
     */
    public static void setHangju(int hangju) {
        mHangju = hangju;
    }

    /**
     * 将一章的文字,按段落分开,存储在集合里
     *
     * @param datas
     * @return
     */
    public static List<String> SeperateByParagph(String datas) {
        List<String> paragphdatas = new ArrayList<>();
        if (datas != null) {
            String[] ps = datas.split("\n");
            for (int i = 0; i < ps.length; i++) {
                ps[i] = ps[i] + "  ";
                paragphdatas.add(ps[i]);
            }
//            if (paragphdatas.size() > 0) {
//                String lastp = paragphdatas.get(paragphdatas.size() - 1).substring(0,
//                        paragphdatas.get(paragphdatas.size() - 1).length() - 1);
//                paragphdatas.remove(paragphdatas.size() - 1);
//                paragphdatas.add(lastp);
//            }
        } else {
            paragphdatas.add(datas);
        }
        return paragphdatas;
    }

    /**
     * 将段落字符串截取成句数据，存储在集合里
     *
     * @param paragraphstr
     * @return
     */
    public static List<String> SeparateParagraphtoLines(String paragraphstr) {

        List<String> linesdata = new ArrayList<>();
        String str = paragraphstr;
        for (; str.length() > 0; ) {
            int nums = getTxtPaint().breakText(str, true, App.getInstance().mScreenWidth, null);
            if (nums <= str.length()) {
                String linnstr = str.substring(0, nums);
                linesdata.add(linnstr);
                str = str.substring(nums, str.length());
            } else {
                linesdata.add(str);
                str = "";
            }
        }
        return linesdata;
    }

    /**
     * 将一章文字全部分解成一行一行的数据,存储在集合里
     * @return
     */
    public static List<String> parsingData() {
        if (mLines.size() == 0) {
            List<String> duan = SeperateByParagph(getData());
            if (duan != null && duan.size() > 0) {
                mLines.clear();
                for (String str : duan) {
                    List<String> line = SeparateParagraphtoLines(str);
                    mLines.addAll(line);
                }
            }
        }
        return mLines;
    }

    /**
     * 一章可以分为多少页码
     * @return
     */
    public static int paging() {
        int count = getHang();
        List<String> lines = parsingData();
        if (lines.size() > 0) {
            int ye = lines.size() / count;
            if (lines.size() % count > 0) {
                ye++;
            }
            return ye;
        }
        return -1;
    }

    /**
     * 一页数据可以显示的行数
     *
     * @return
     */
    public static int getHang() {
        int height = App.getInstance().mScreenHeight - mY;
        return height / (getFontHeight() + mHangju);
    }

    /**
     * @param index         页码下标 从0开始
     * @return
     */
    public static Bitmap getBitMap(int index) {
        long a = System.currentTimeMillis();
        Bitmap bitmap = Bitmap.createBitmap(App.getInstance().mScreenWidth, App.getInstance().mScreenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(bColor);
        Paint paint = getTxtPaint();
        int FontHeight = getFontHeight();
        Log.e("txtread", "FontHeight: " + FontHeight);
        List<String> lines = parsingData();
        Log.e("txtread", "lines.size(): " + lines.size());
        int x = mX;
        int y = mY;
        int count = getHang();
        Log.e("txtread", "count: " + count);
        int i = index * count;
        int end = count * (index + 1);
        for (; i < end; i++) {
            if (i < lines.size()) {
                canvas.drawText(lines.get(i), x, y, paint);
                y = y + FontHeight + mHangju;
            } else {
                break;
            }
        }
        long b = System.currentTimeMillis() - a;
        Log.e("txtread", "getBitmap()耗时: " + b);
        return bitmap;
    }
}
