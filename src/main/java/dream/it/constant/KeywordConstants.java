package dream.it.constant;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 男表女表品牌关键词已经分开
 *
 */
public enum KeywordConstants {

    /**
     * 女表相关关键词
     */
    WOMEN_WATCH("fitbit,apple,bark,fit bit,enewton,phoibos,brandy,foxgirl,nibosi,hermès,aobei,joyas,civo,hermes,seiko,skagen,speidel,tag heuer,pulsar,larsson,van cleef,dw,larsson,fiyta,bulgari,charm,salvador,bering,rosegold,raymond,vivienne,boderry,lola rose,longines,david,pavoi,xoxo,mondaine,ferragamo,disney,breda,aesthetic,moissanite,geneva,brighton,polo,trump,steve madden,oferta,mvmt,guess,burberry,serpent,montre,ecodrive,dainty,philippe,panthere,nixon,elgin,tissot,reloj acero,liebeskind,sketchers,armani,anna,kate,armitron,skmei,pandora,jbw,mk,jessica,olevs,pagani,berny,timex,micheal,cavalli,calvin,stretch,coach,lacoste,swiss,skechers,ck,reloj fosil,kate,kenneth,ann klein,michael,michel,boho,swatch,victoria,scott,tommy,michael kors,ofertas,dkny,bulova,peugeot,citizen,omega,tory burch,reloges,olivia burton,rado,relic,michael kors,nine west,stuhrling,movado,ninewest,fossil,goth,betsey johnson,chanel,technomarine,michele,versace,bvlgari,daniel,swarovski,anne,invicta,michele,titan,cartier,sekonda,cheetah,casio,gucci,rolex,shark,tank,fendi,snake"),

    /**
     * 男表相关关键词
     */
    MEN_WATCH("gunmetal,sunnto,darth,bremont,victorinox,poljot,puma,biden,loreo,casotime,carrera,stellaya,egqinr,liurundianqi,lenovo,cronos,thompson,anime,bmw,timberland,pindu,emt,amaztim,tagheuer,grv,trausi,geoffrey,motorola,tritium,pindows,crrju,bell and ross,smith and wesson,ferrari,north,vincero,wyze,carbonox,glucose,christopher,smael,lige,jacob and co,nike,ciga,glory,lorus,caravelle,mavado,aviator,senbono,feice,nomos,wenger,zenith,panzera,carbinox,braun,avi-8,hugo,stauer,tritium,shinola,festina,tozo,letsfit,luxium,samsung,baltany,huawei,ratio,christian,victorinox,vaer,CMF,nothing,breitling,victorinox,atari,diesel,tudor,carbinox,luminox,smart,garmin,miu,f91w,casuo,gunmetal,detomaso,socico,cross,aiyishi,lenqin,affordable,melted,twisted,read,senstone,classy,lunaro,sternglas,marc jacobs,ap royal oak,mulco,casuo,edifice,groomsmen,gunmetal,zelos,oakley,emt,holzkern,solar,ufc,avi-8,heritor,supremo,pintime,puma,stylish,holuns,cassio,bell,gufts,eink,edc,milano,benrus,audemars,hmt,fosil,nubeo,invictus,diesel mega chief,bremont,zavion,skeleton,darth vade,caterpillar,bmw,smith and wesson,mont blanc,caterpillar,captain america,calithe phantom,sanda,shinola,manual wind,stauer,tufina,kinetic,james bond,bugatti,ralph christian,onola,blue angels,biden,adidas,oakley,montblanc,vaer,ferrari,cronos,urwerk,kuoe,hublot,kuoe,bmw,accutron,timberland,ciga design,foxbox,cainte,geoffrey,tritium,invita,forsining,brew,bell and ross,calculator,bugatti,megir,police,paul rich,ferrari,mf mini focus,vincero,lige,jacob,baltic,aviator,wenger,zenith,hugo boss,festina,shinola,roulette,feice,sapphero,hestur,zenith,burei,hugo boss,benyar,shinola,wooden,festina,mulco,digital,baltany,panerai,foxgirl,nibosi,hermès,aobei,joyas,civo,hermes,seiko,skagen,speidel,tag heuer,pulsar,larsson,van cleef,dw,larsson,fiyta,bulgari,charm,salvador,bering,rosegold,raymond,vivienne,boderry,lola rose,longines,david,pavoi,xoxo,mondaine,ferragamo,disney,breda,aesthetic,moissanite,geneva,brighton,polo,trump,steve madden,oferta,mvmt,guess,burberry,serpent,montre,ecodrive,dainty,philippe,panthere,nixon,elgin,tissot,reloj acero,liebeskind,sketchers,armani,anna,kate,armitron,skmei,pandora,jbw,mk,jessica,olevs,pagani,berny,timex,micheal,cavalli,calvin,stretch,coach,lacoste,swiss,skechers,ck,reloj fosil,kate,kenneth,ann klein,michael,michel,boho,swatch,victoria,scott,tommy,michael kors,ofertas,dkny,bulova,peugeot,citizen,omega,tory burch,reloges,olivia burton,rado,relic,michael kors,nine west,stuhrling,movado,ninewest,fossil,goth,betsey johnson,chanel,technomarine,michele,versace,bvlgari,daniel,swarovski,anne,invicta,michele,titan,cartier,sekonda,cheetah,casio,gucci,rolex,shark,tank,fendi,snake");

    private final String keywords;

    KeywordConstants(String keywords) {
        this.keywords = keywords;
    }

    /**
     * 获取原始关键词字符串（逗号分隔）
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * 获取去重后的关键词集合
     */
    public Set<String> getKeywordSet() {
        return Arrays.stream(keywords.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
