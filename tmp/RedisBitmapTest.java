    public static void main(String[] args){
        String rs = HttpClientManager.getInstance().sendRequestAsGet(REDISAPI);
        System.out.println(rs);

        RedisUtil ru = new RedisUtil();
        ru.init();
        Jedis jedis = ru.getJedisPool().getResource();

//        long s = System.currentTimeMillis();
//        for(int i=0;i<20*10000;i++) {
//            int j = i % 2;
//            jedis.setbit("test2", i, j==1);
//            if(i%1000 == 0) {
//                System.out.println(i);
//            }
//        }
//        long cost1 = System.currentTimeMillis() - s;
//        System.out.println(cost1);

//        long st = System.currentTimeMillis();
//        for(int i=0;i<2000;i++) {
//            Boolean test2 = jedis.getbit("test2", 5);
//        }
//        long cost = System.currentTimeMillis() - st;
//        System.out.println(cost);

        Object eval = jedis.eval("local t=redis.call(\"getbit\", \"test2\", 5); return t");
        System.out.println(eval);

        eval = jedis.eval("return redis.call(\"getbit\", \"test2\", 0)");
        System.out.println(eval);

        String c = //bd65aef4dab1eb1ed922a249678b929485238cb6
                "local result ={} \n" +
                "for i = 1,#(KEYS) do \n" +
                "   result[i]= redis.call('getbit','test2',KEYS[i]) \n" +
                "end \n" +
                "return result";
        String luahash = "bd65aef4dab1eb1ed922a249678b929485238cb6";


        ArrayList<Long> eval2 = (ArrayList<Long>)jedis.eval(c, 3, "4", "5", "6");
        System.out.println(eval2);

//        String luahash = jedis.scriptLoad(c);
//        System.out.println(luahash);


        long st2 = System.currentTimeMillis();
        int paramSize2 = 10000;
        String[] params2 = new String[paramSize2];
        for(int i=0; i<paramSize2; i++){
            params2[i] = String.valueOf(i);
        }
        eval2 = (ArrayList<Long>)jedis.evalsha(luahash, paramSize2, params2);
        long cost2 = System.currentTimeMillis() - st2;
        System.out.println(cost2);
//        System.out.println(eval2);



        long st = System.currentTimeMillis();
        int paramSize = 10000;
        String[] params = new String[paramSize];
        for(int i=19*10000; i<19*10000+paramSize; i++){
            params[i-19*10000] = String.valueOf(i);
        }
        eval2 = (ArrayList<Long>)jedis.evalsha(luahash, paramSize, params);
        long cost = System.currentTimeMillis() - st;
        System.out.println(cost);
//        System.out.println(eval2);

        jedis.close();
    }

