package controller;

import entity.FixTime;
import entity.UserTable;
import entity.register;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class main {


    public static void main(String[] args) {
        //是否登录
        boolean isRegister = false;

        //银行id
        final String BANK_ID = "QIFEI";

        Double rate = 1.1;

        String userId = "";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName = bank","sa","123456");
        }catch (Exception e){
            e.printStackTrace();
        }

        //是否退出
        boolean isExit = false;

        while (true) {
            if(isExit){
                break;
            }
            System.out.println("=========银行界面=========");
            System.out.println("\t1、用户登录");
            System.out.println("\t2、用户开户");
            System.out.println("\t3、活期存款");
            System.out.println("\t4、活期取款");
            System.out.println("\t5、定期存款");
            System.out.println("\t6、定期取款");
            System.out.println("\t7、用户登出");
            System.out.println("\t8、退出");
            System.out.print("\t请选择（1-8）：");
            Scanner scanner = new Scanner(System.in);
            int i = scanner.nextInt();
            switch (i){
                case 1:
                    if(isRegister){
                        System.out.println("您已经登录");
                        break;
                    }
                    try {
                        System.out.println("请输入账号");
                        String account = scanner.next();
                        System.out.println("请输入密码");
                        String password = scanner.next();
                        ps = con.prepareStatement("select * from UserTable where account = ? and password=?");
                        ps.setString(1,account);
                        ps.setString(2,password);
                        rs = ps.executeQuery();
                        UserTable user = null;
                        while (rs.next()){
                            String id = rs.getString("id");
                            account = rs.getString("account");
                            password = rs.getString("password");
                            BigDecimal decimal = rs.getBigDecimal("decimal");
                            user = new UserTable(id,account,password,decimal);
                        }
                        if(user==null){
                            System.out.println("用户不存在,请检查账号密码");
                            break;
                        }
                        isRegister=true;
                        userId=user.getId();
                        System.out.println(" 您已成功登录,账号为:"+user);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    if(isRegister){
                        try {
                            //查询用户是否已经开户,若有则提示不可重复开户
                            ps = con.prepareStatement("select * from register where userID = ? and bankId=?");
                            ps.setString(1,userId);
                            ps.setString(2,BANK_ID);
                            rs = ps.executeQuery();
                            if(rs.next()){
                                System.out.println("您已经开户,不允许重复开户");
                                break;
                            }
                            String s = "";
                            ps = con.prepareStatement("insert into register values (?,?,?,?)");
                            ps.setString(1,UUID.randomUUID().toString());
                            ps.setString(2,BANK_ID);
                            ps.setString(3,userId);
                            ps.setString(4,s =UUID.randomUUID().toString());
                            int result = ps.executeUpdate();
                            if(result>0){
                                System.out.println("开户成功,您的开户号为"+s);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("请先登录");
                    }
                    break;
                case 3:
                    if(isRegister){
                        try {
                            System.out.println("请输入您要存款的金额(单位:元)");
                            scanner = new Scanner(System.in);
                            Long money = scanner.nextLong();
                            ps = con.prepareStatement("select * from UserTable where id = ? and decimal>=?");
                            ps.setString(1,userId);
                            ps.setBigDecimal(2,new BigDecimal(money));
                            rs = ps.executeQuery();
                            UserTable user = null;
                            while (rs.next()){
                                String id = rs.getString("id");
                                String account = rs.getString("account");
                                String password = rs.getString("password");
                                BigDecimal decimal = rs.getBigDecimal("decimal");
                                user = new UserTable(id,account,password,decimal);
                            }
                            if(user==null){
                                System.out.println("您没有足够钱用于存款");
                                break;
                            }
                            ps = con.prepareStatement("update userTable set decimal=? where id = ?");
                            ps.setBigDecimal(1,user.getDecimal().subtract(new BigDecimal(money)));
                            ps.setString(2,userId);
                            ps.executeUpdate();
                            System.out.println("扣减用户余额成功");
                            ps = con.prepareStatement("select * from \"current\" where userId = ?");
                            ps.setString(1,userId);
                            rs = ps.executeQuery();
                            if(rs.next()){
                                System.out.println("已有存款,将从该存款中直接增加存款");
                                ps = con.prepareStatement("update \"current\" set decimal = ? where userId = ?");
                                BigDecimal decimal = rs.getBigDecimal("decimal");
                                BigDecimal add = new BigDecimal(money).add(decimal);
                                ps.setBigDecimal(1,add);
                                ps.setString(2,userId);
                                ps.executeUpdate();
                                System.out.println("存款成功");
                                break;
                            }
                            ps = con.prepareStatement("insert into \"current\" values (?,?,?,?)");
                            ps.setString(1,UUID.randomUUID().toString());
                            ps.setString(2,BANK_ID);
                            ps.setString(3,userId);
                            ps.setBigDecimal(4,new BigDecimal(money));
                            ps.executeUpdate();
                            System.out.println("存款成功");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("请先登录");
                    }
                    break;
                case 4:
                    if(isRegister){
                        try {
                            System.out.println("请输入您要取款的金额");
                            scanner = new Scanner(System.in);
                            Long money = scanner.nextLong();
                            ps = con.prepareStatement("select * from \"current\"  where userID = ?");
                            ps.setString(1,userId);
                            rs = ps.executeQuery();
                            if(rs.next()){
                                BigDecimal decimal = rs.getBigDecimal("decimal");
                                Long allMoney = (long) decimal.intValue();
                                if(allMoney<money){
                                    System.out.println("您的存款不足");
                                    break;
                                }

                                System.out.println("扣减存储余额");
                                ps = con.prepareStatement("update \"current\" set decimal = ? where userId = ?");
                                ps.setBigDecimal(1,rs.getBigDecimal("decimal").subtract(new BigDecimal(money)));
                                ps.setString(2,userId);
                                ps.executeUpdate();

                                System.out.println("往用户中增加余额");
                                ps = con.prepareStatement("select * from UserTable where id = ?");
                                ps.setString(1,userId);
                                rs = ps.executeQuery();
                                rs.next();
                                ps = con.prepareStatement("update userTable set decimal=? where id = ?");
                                BigDecimal add = new BigDecimal(money).add(rs.getBigDecimal("decimal"));
                                ps.setBigDecimal(1,add);
                                ps.setString(2,userId);
                                ps.executeUpdate();
                                System.out.println("取款成功");
                            }else {
                                System.out.println("您还没有进行存款,不能进行取款操作");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else {
                        System.out.println("请先登录");
                    }
                    break;
                case 5:
                    if(isRegister){
                        try {
                            System.out.println("请输入您要存款的金额");
                            scanner = new Scanner(System.in);
                            Long money = scanner.nextLong();
                            //查询金额够不够
                            ps = con.prepareStatement("select * from UserTable where id = ? and decimal>=?");
                            ps.setString(1,userId);
                            ps.setBigDecimal(2,new BigDecimal(money));
                            rs = ps.executeQuery();
                            UserTable user = null;
                            while (rs.next()){
                                String id = rs.getString("id");
                                String account = rs.getString("account");
                                String password = rs.getString("password");
                                BigDecimal decimal = rs.getBigDecimal("decimal");
                                user = new UserTable(id,account,password,decimal);
                            }
                            if(user==null){
                                System.out.println("您没有足够钱用于存款");
                                break;
                            }
                            System.out.println("请输入您存款的时间(单位:年)");
                            scanner = new Scanner(System.in);
                            Long time = scanner.nextLong();
                            if(time<1){
                                System.out.println("您输入的时间不合法,请重新输入");
                                break;
                            }
                            System.out.println("扣除用户余额");
                            ps = con.prepareStatement("update userTable set decimal=? where id = ?");
                            ps.setBigDecimal(1,user.getDecimal().subtract(new BigDecimal(money)));
                            ps.setString(2,userId);
                            ps.executeUpdate();
                            ps = con.prepareStatement("insert into FixTime values (?,?,?,?,?,?)");
                            ps.setString(1,UUID.randomUUID().toString());
                            ps.setString(2,BANK_ID);
                            ps.setString(3,userId);
                            ps.setBigDecimal(4,new BigDecimal(money));
                            ps.setBigDecimal(5, BigDecimal.valueOf(money*rate));
                            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            String year = (Integer.parseInt(format.substring(0, 4)) + time)+"";
                            String substring = format.substring(4);
                            String s = year + substring;
                            ps.setString(6,s);
                            ps.executeUpdate();
                            System.out.println("定期存款完成");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else {
                        System.out.println("请先登录");
                    }
                    break;
                case 6:
                    if(isRegister){
                        try {
                            ps = con.prepareStatement("select * from FixTime where userId = ?");
                            ps.setString(1,userId);
                            rs = ps.executeQuery();
                            List<FixTime> suit = new ArrayList<>();
                            List<FixTime> notSuit = new ArrayList<>();
                            while (rs.next()){
                                String id = rs.getString("id");
                                String bankId = rs.getString("bankId");
                                String userId1 = rs.getString("userId");
                                BigDecimal decimal = rs.getBigDecimal("decimal");
                                BigDecimal allMoney = rs.getBigDecimal("allMoney");
                                String days = rs.getString("days");
                                FixTime fixTime = new FixTime(id,bankId,userId1,decimal,allMoney,days);
                                String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                if(judge(days,format)){
                                    suit.add(fixTime);
                                }else {
                                    notSuit.add(fixTime);
                                }
                            }
                            //查询符合条件的定期存款,有则显示,无则提示
                            if(suit.isEmpty()){
                                System.out.println("目前没有符合条件的定期存款可以取出");
                            }else {
                                System.out.println("以下定期存款符合条件,请输入想要取出的id,输入y结束");
                                System.out.println(suit);
                                scanner = new Scanner(System.in);
                                List<String> ids = new ArrayList<>();
                                String s ="";
                                while (!s.equals("y")){
                                    s = scanner.next();
                                    ids.add(s);
                                }
                                for (String id : ids) {
                                    ps = con.prepareStatement("select * from FixTime where id = ?");
                                    ps.setString(1,id);
                                    rs = ps.executeQuery();
                                    if(rs.next()){
                                        System.out.println("将id为:"+id+"的定期存款转移到用户余额中");
                                        BigDecimal allMoney = rs.getBigDecimal("allMoney");
                                        ps = con.prepareStatement("select * from UserTable where id = ?");
                                        ps.setString(1,userId);
                                        rs = ps.executeQuery();
                                        rs.next();
                                        BigDecimal decimal = rs.getBigDecimal("decimal");
                                        ps = con.prepareStatement("update userTable set decimal=? where id = ?");
                                        ps.setBigDecimal(1,decimal.add(allMoney));
                                        ps.setString(2,userId);
                                        ps.executeUpdate();
                                        System.out.println("id为:"+id+"的数据转移成功");
                                        System.out.println("删除定期存款的记录");
                                        ps = con.prepareStatement("delete from FixTime where id = ?");
                                        ps.setString(1,id);
                                        ps.executeUpdate();
                                        System.out.println("删除成功");
                                    }else {
                                        System.out.println("不存在该定期存款的数据,请检查您的输入");
                                    }
                                }
                            }

                            //查询符合条件的定期存款,有则显示,无则提示
                            if(notSuit.isEmpty()){
                                System.out.println("目前没有不符合条件的定期存款可以取出");
                            }else {
                                System.out.println("以下定期存款不符合取款条件,若取出则不会获得任何利润,请输入想要取出的id,输入y结束");
                                System.out.println(notSuit);
                                scanner = new Scanner(System.in);
                                List<String> ids = new ArrayList<>();
                                String s ="";
                                while (!s.equals("y")){
                                    s = scanner.next();
                                    ids.add(s);
                                }
                                for (String id : ids) {
                                    ps = con.prepareStatement("select * from FixTime where id = ?");
                                    ps.setString(1,id);
                                    rs = ps.executeQuery();
                                    if(rs.next()){
                                        System.out.println("将id为:"+id+"的定期存款转移到用户余额中");
                                        BigDecimal allMoney = rs.getBigDecimal("decimal");
                                        ps = con.prepareStatement("select * from UserTable where id = ?");
                                        ps.setString(1,userId);
                                        rs = ps.executeQuery();
                                        rs.next();
                                        BigDecimal decimal = rs.getBigDecimal("decimal");
                                        ps = con.prepareStatement("update userTable set decimal=? where id = ?");
                                        ps.setBigDecimal(1,decimal.add(allMoney));
                                        ps.setString(2,userId);
                                        ps.executeUpdate();
                                        System.out.println("id为:"+id+"的数据转移成功");
                                        System.out.println("删除定期存款的记录");
                                        ps = con.prepareStatement("delete from FixTime where id = ?");
                                        ps.setString(1,id);
                                        ps.executeUpdate();
                                        System.out.println("删除成功");
                                    }else {
                                        System.out.println("不存在该定期存款的数据,请检查您的输入");
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else {
                        System.out.println("请先登录");
                    }
                    break;
                case 7:
                    if(isRegister){
                        isRegister=false;
                        userId = "";
                        System.out.println("您已成功登出");
                    }else {
                        System.out.println("请先登录");
                    }
                    break;
                case 8:
                    isExit=true;
                    break;
                default:
                    System.out.println("数字不合法,请重新输入");
            }
        }
    }

    private static boolean judge(String days, String format) {
        Integer yearEnd = Integer.parseInt(days.substring(0, 4));
        Integer monthEnd = Integer.parseInt(days.substring(5, 7));
        Integer dayEnd = Integer.parseInt(days.substring(8));
        Integer yearNow = Integer.parseInt(format.substring(0, 4));
        Integer monthNow = Integer.parseInt(format.substring(5, 7));
        Integer dayNow = Integer.parseInt(format.substring(8));
        //年份高可以取出
        if(yearNow>yearEnd){
            return true;
        }
        //年份相等月数更高可以取出
        if(yearNow.equals(yearEnd) && monthNow>monthEnd){
            return true;
        }
        //年月相等天数更高可以取出
        if(yearNow.equals(yearEnd) && monthNow.equals(monthEnd) && dayNow>dayEnd){
            return true;
        }
        //其他情况均不可取出
        return false;
    }

    private static boolean userRegister() {
        System.out.println("请输入账号");
        Scanner scanner = new Scanner(System.in);
        String account = scanner.next();
        System.out.println("请输入密码");
        scanner = new Scanner(System.in);
        String password = scanner.next();
        //此处模拟数据库比对
        if(account.equals("123") && password.equals("123")){
            return true;
        }
        System.out.println("请检查您输入的账号密码");
        return false;
    }
}
