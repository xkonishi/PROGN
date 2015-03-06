/*------------------------------------------------------------------------------
 * PROGNER EX for Java Servlet
 * Copyright(C) 2008 - 2014 Canon IT Solutions Inc. All rights reserved.
 *----------------------------------------------------------------------------*/
package jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2060.service;

import java.util.List;

import jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2000.service.CodeService;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2000.service.ListService;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.constants.AppConstants;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.dao.APTemplateGeneralJDBCUtilDao;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.dao.exception.APTemplateSQLException;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.dto.BaseServiceParameters;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.service.BaseService;
import jp.co.canonits.prognerex.core.common.constants.PrognerCoreCommonConstants;
import jp.co.canonits.prognerex.core.common.exception.LogicalException;
import jp.co.canonits.prognerex.core.common.message.MessageManager;
import jp.co.canonits.prognerex.core.common.utility.NumberUtility;
import jp.co.canonits.prognerex.core.common.utility.resource.ResourceFileNotFoundException;
import jp.co.canonits.prognerex.core.dao.resultset.SimpleResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>案件台帳検索サービス</p>
 * 
 * @author Canon IT Solutions Inc. R&amp;D Center
 * @version 2.3
 */
public class CC2060S02 extends BaseService{

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ログインスタンス
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CC2060S02.class);

    /**
     * メッセージファイルクラスファイル
     */
    protected static final String messageFileClassPath = "jp/co/canonits/prognerex/aptemplate_desktopaplike/CC2060/service/CC2060S02";

    /**
     * メッセージファイルの読み込みを行います
     */
    static{
        try{
            MessageManager.loadMessages(messageFileClassPath, PrognerCoreCommonConstants.LANGAGE_LOCALE_LIST);
        }catch(ResourceFileNotFoundException e){
            throw new IllegalStateException(e);
        }
    }

    // --------------------------------------------------------------------------
    // 内部定数
    // --------------------------------------------------------------------------

    /**
     * 検索上限数
     */
    protected final int MAX_ROWS = 100;

    // ------------------------------------------------------
    // SQL
    // ------------------------------------------------------
    
    protected final String SQL_SELECT_TABLE =
        "SELECT COMPANYCD"
      +      ", MENUID"
      +      ", COALESCE(TO_CHAR(UPDDATE,'YYYY/MM/DD HH24:MI:SS'),' ') AS UPDDATE"
      +      ", MENUKBN"
      +      ", MAJORID"
      +      ", MINORID"
      +      ", SCREENID" 
      +      ", MENUNO"
      +      ", MENUNM"
      +      ", CLASSNM"
      + " FROM CCMENU"      
      + " WHERE COMPANYCD = :COMPANYCD"      
      + " ORDER BY COMPANYCD, MENUID, MENUKBN, MENUNO";
    
    protected final String SQL_LOCK_TABLE =
        "SELECT COMPANYCD"
      +      ", MENUID"
      + " FROM CCMENU"
      + " WHERE COMPANYCD = :COMPANYCD"
      +   " AND MENUID = :MENUID"
      +   " AND REVISION = :REVISION"
      + " FOR UPDATE NOWAIT";

    protected final String SQL_INSERT_TABLE =
        "INSERT INTO CCMENU ("
      +      "  COMPANYCD"
      +      ", MENUID"
      +      ", INSDATE, INSUSRID, INSPGID, UPDDATE, UPDUSRID, UPDPGID, REVISION"
      +      ", MENUKBN"
      +      ", MAJORID"
      +      ", MINORID"
      +      ", SCREENID" 
      +      ", MENUNO"
      +      ", MENUNM"
      +      ", CLASSNM"
      + ") VALUES ("
      +      "  :COMPANYCD"
      +      ", :ANKENNO"
      +      ", SYSDATE, :INSUSRID, :INSPGID, SYSDATE, :UPDUSRID, :UPDPGID, 1"
      +      ", :MENUKBN"
      +      ", :MAJORID"
      +      ", :MINORID"
      +      ", :SCREENID" 
      +      ", :MENUNO"
      +      ", :MENUNM"
      +      ", :CLASSNM"
      + ")";
    
    protected final String SQL_UPDATE_TABLE =
        "UPDATE CCMENU"
      +   " SET UPDDATE = SYSDATE"
      +      ", UPDUSRID = :UPDUSRID"
      +      ", UPDPGID = :UPDPGID"
      +      ", REVISION = REVISION + 1"
      +      ", MENUKBN = :MENUKBN"
      +      ", MAJORID = :MAJORID"
      +      ", MINORID = :MINORID"
      +      ", SCREENID = :SCREENID" 
      +      ", MENUNO = :MENUNO"
      +      ", MENUNM = :MENUNM"
      +      ", CLASSNM = :CLASSNM"
      + " WHERE COMPANYCD = :COMPANYCD"
      +   " AND MENUID = :MENUID";    

    protected final String SQL_DELETE_TABLE =
            "DELETE FROM CCMENU"
          + " WHERE COMPANYCD = :COMPANYCD"
          +   " AND MENUID = :MENUID";
    

    /**
     * 案件台帳取得SQL
     */
    protected final String SQL_SELECT_CPANKENDAICHO =
        "SELECT A.ANKENNO"
      +      ", A.ANKENKBN, A.EIGYOKBN, A.SHINCHOKUKBN"
      +      ", A.ANKENMEI, A.JUCHUBI"
      +      ", A.EIGYOTANTOCD, B.USERNM AS EIGYOTANTOMEI"
      +      ", A.JUCHUKINGAKU"
      +      ", A.REVISION"
      +  " FROM CPANKENDAICHO A"
      +       " LEFT OUTER JOIN CCLOGIN B"
      +         " ON A.EIGYOTANTOCD = B.USERID"
      + " WHERE A.COMPANYCD = :COMPANYCD"
      +   " AND A.ANKENNO LIKE '%' || COALESCE(:ANKENNO, A.ANKENNO) || '%'"
      +   " AND A.ANKENMEI LIKE '%' || COALESCE(:ANKENMEI, A.ANKENMEI) || '%'"
      +   " AND A.SHINCHOKUKBN = COALESCE(:SHINCHOKUKBN, A.SHINCHOKUKBN)"
      +   " AND COALESCE(A.JUCHUBI, ' ') BETWEEN COALESCE(:JUCHUBI_FROM, COALESCE(A.JUCHUBI, ' '))"
      +                                    " AND COALESCE(:JUCHUBI_TO, COALESCE(A.JUCHUBI, ' '))"
      +   " AND COALESCE(A.EIGYOTANTOCD, ' ') = COALESCE(:EIGYOTANTOCD, COALESCE(A.EIGYOTANTOCD, ' '))"
      + " ORDER BY A.ANKENNO";

    /**
     * 案件台帳排他制御SQL
     */
    protected final String SQL_LOCK_CPANKENDAICHO =
        "SELECT A.ANKENNO"
      +  " FROM CPANKENDAICHO A"
      + " WHERE A.COMPANYCD = :COMPANYCD"
      +   " AND A.ANKENNO = :ANKENNO"
      +   " AND A.REVISION = :REVISION"
      + " FOR UPDATE NOWAIT";

    /**
     * 案件台帳登録SQL
     */
    protected final String SQL_INSERT_CPANKENDAICHO =
        "INSERT INTO CPANKENDAICHO ("
      +     "COMPANYCD, ANKENNO"
      +   ", INSDATE, INSUSRID, INSPGID, UPDDATE, UPDUSRID, UPDPGID, REVISION"
      +   ", ANKENKBN, EIGYOKBN, SHINCHOKUKBN"
      +   ", ANKENMEI, JUCHUBI"
      +   ", EIGYOBUMONCD, EIGYOTANTOCD"
      +   ", MITSUMORIKINGAKU, JUCHUKINGAKU, SEIZOGENKA"
      +   ", ARARIGAKU, ARARIRITSU"
      + ") VALUES ("
      +     ":COMPANYCD, :ANKENNO"
      +   ", SYSDATE, :INSUSRID, :INSPGID, SYSDATE, :UPDUSRID, :UPDPGID, 1"
      +   ", :ANKENKBN, :EIGYOKBN, :SHINCHOKUKBN"
      +   ", :ANKENMEI, :JUCHUBI"
      +   ", :EIGYOBUMONCD, :EIGYOTANTOCD"
      +   ", 0, COALESCE(:JUCHUKINGAKU, 0), 0, 0, 0"
      + ")";

    /**
     * 案件台帳更新SQL
     */
    protected final String SQL_UPDATE_CPANKENDAICHO =
        "UPDATE CPANKENDAICHO"
      +   " SET UPDDATE = SYSDATE"
      +      ", UPDUSRID = :UPDUSRID"
      +      ", UPDPGID = :UPDPGID"
      +      ", REVISION = REVISION + 1"
      +      ", ANKENMEI = :ANKENMEI"
      +      ", ANKENKBN = :ANKENKBN"
      +      ", EIGYOKBN = :EIGYOKBN"
      +      ", SHINCHOKUKBN = :SHINCHOKUKBN"
      +      ", EIGYOBUMONCD = :EIGYOBUMONCD"
      +      ", EIGYOTANTOCD = :EIGYOTANTOCD"
      +      ", JUCHUBI = :JUCHUBI"
      +      ", JUCHUKINGAKU = COALESCE(:JUCHUKINGAKU, 0)"
      + " WHERE COMPANYCD = :COMPANYCD"
      +   " AND ANKENNO = :ANKENNO";

    /**
     * 案件台帳削除SQL
     */
    protected final String SQL_DELETE_CPANKENDAICHO =
        "DELETE FROM CPANKENDAICHO"
      + " WHERE COMPANYCD = :COMPANYCD"
      +   " AND ANKENNO = :ANKENNO";

    // --------------------------------------------------------------------------
    // 公開クラス
    // --------------------------------------------------------------------------

    // ------------------------------------------------------
    // I/Oパラメータ
    // ------------------------------------------------------
    
    public class Condition2 extends BaseServiceParameters{

        /**
         * シリアルバージョンUID
         */
        private static final long serialVersionUID = 1L;
        
    }

    /**
     * 条件部
     * 
     * @author Canon IT Solutions Inc. R&amp;D Center
     * @version 2.3
     */
    public class Condition extends BaseServiceParameters{

        /**
         * シリアルバージョンUID
         */
        private static final long serialVersionUID = 1L;

        /**
         * 営業担当コード
         */
        public String EIGYO_TANTO_CD = "EIGYO_TANTO_CD";

        /**
         * 案件ＮＯ
         */
        public String ankenNo;

        /**
         * 案件名
         */
        public String ankenMei;

        /**
         * 進捗区分
         */
        public String shinchokuKbn;

        /**
         * 営業担当コード
         */
        public String eigyoTantoCd;

        /**
         * 営業担当名
         */
        public String eigyoTantoMei;

        /**
         * 受注日（Ｆｒｏｍ）
         */
        public String juchubiFrom;

        /**
         * 受注日（Ｔｏ）
         */
        public String juchubiTo;
    }
    
    
    public class Detail2 extends BaseServiceParameters{

        /**
         * シリアルバージョンUID
         */
        private static final long serialVersionUID = 1L;
        
        public String COMPANYCD = "COMPANYCD";
        public String MENUID = "MENUID";
        
        public String companyCode;
        public String menuId;
        public String updDate;
        public String menuKbn;
        public String majorId;
        public String minorId;
        public String screenId;
        public int menuNo;
        public String menuNm;
        public String classNm;        

        /**
         * リビジョン
         */
        public int revision;

        /**
         * 処理モード
         */
        public int operation;
    }

    /**
     * 明細部I/Oパラメータ
     * 
     * @author Canon IT Solutions Inc. R&amp;D Center
     * @version 2.3
     */
    public class Detail extends BaseServiceParameters{

        /**
         * シリアルバージョンUID
         */
        private static final long serialVersionUID = 1L;        

        /**
         * 案件ＮＯ
         */
        public String ANKEN_NO = "ANKEN_NO";

        /**
         * 営業担当コード
         */
        public String EIGYO_TANTO_CD = "EIGYO_TANTO_CD";

        /**
         * 案件ＮＯ
         */
        public String ankenNo;

        /**
         * 案件区分
         */
        public String ankenKbn;

        /**
         * 営業区分
         */
        public String eigyoKbn;

        /**
         * 進捗区分
         */
        public String shinchokuKbn;

        /**
         * 案件名
         */
        public String ankenMei;

        /**
         * 営業担当コード
         */
        public String eigyoTantoCd;

        /**
         * 営業担当名
         */
        public String eigyoTantoMei;

        /**
         * 受注日
         */
        public String juchubi;

        /**
         * 受注金額
         */
        public double juchuKingaku;

        /**
         * リビジョン
         */
        public int revision;

        /**
         * 処理モード
         */
        public int operation;

    }

    /**
     * リスト要素
     * 
     * @author Canon IT Solutions Inc. R&amp;D Center
     * @version 2.3
     */
    public class Choices extends BaseServiceParameters{

        /**
         * シリアルバージョンUID
         */
        private static final long serialVersionUID = 1L;

        /**
         * 案件区分
         */
        public List<String[]> ankenKbn;

        /**
         * 営業区分
         */
        public List<String[]> eigyoKbn;

        /**
         * 進捗区分
         */
        public List<String[]> shinchokuKbn;

    }

    // --------------------------------------------------------------------------
    // 公開メソッド
    // --------------------------------------------------------------------------

    /**
     * <p>起動条件 : コンストラクタ</p>
     * <p>処理概要 : サービスを生成</p>
     * 
     */
    public CC2060S02(){

        // --------------------------------------------------
        // プログラム情報
        // --------------------------------------------------
        // 処理ID
        this.setOperationId("CC2060");
        // プログラムID
        this.setProgramId("CC2060S02");
        // プログラム名
        this.setProgramName("案件台帳検索");
        // プログラムリビジョン
        this.setProgramRevision("01.00");
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 初期処理を実行する</p>
     * 
     * @param choices (O)リストボックスデータ
     * @return 初期処理の成否
     * @throws LogicalException 論理的例外
     */
    public boolean executeInit(Choices choices) throws LogicalException{

        boolean ret = false;

        // --------------------------------------------------
        // 開始処理の実行
        // --------------------------------------------------
        try{
            if(!this.initializeService()){
                return false;
            }
        }catch(LogicalException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Service initialization is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }
            throw e;
        }

        // --------------------------------------------------
        // 検索の実行
        // --------------------------------------------------
        try{
            // リスト取得部品を生成
            ListService service = new ListService();
            service.setLoginModel(this.getLoginModel());
            service.setClientLocale(this.getClientLocale());
            // 案件区分
            choices.ankenKbn = service.getCodeListFromView(this.getLoginModel().getCompanyCode(), AppConstants.CODE_P_ANKENKBN, "案件区分");
            // 営業区分
            choices.eigyoKbn = service.getCodeListFromView(this.getLoginModel().getCompanyCode(), AppConstants.CODE_P_EIGYOKBN, "営業区分");
            // 進捗状況
            choices.shinchokuKbn = service.getCodeListFromView(this.getLoginModel().getCompanyCode(), AppConstants.CODE_P_SHINCHOKUKBN, "進捗状況");

            // メッセージ表示(初期処理正常終了)
            this.setMessage("CMN00006", "初期処理");
            // ログ出力(初期処理正常終了)
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("Service initialization is success.");
            }
            ret = true;

        }catch(LogicalException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Service initialization is failed. Some runtime exception happen. Please check stacktrace." + e.getMessage());
            }
            throw e;
        }

        // --------------------------------------------------
        // 終了処理の実行
        // --------------------------------------------------
        this.finalizeService();

        return ret;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 検索処理を実行する</p>
     * 
     * @param condition (I)条件データ
     * @param details (O)明細データ
     * @return 検索本処理の成否
     * @throws LogicalException 論理的例外
     */
    public boolean executeSearch(Condition2 condition, List<Detail2> details) throws LogicalException{

        boolean ret = false;

        // --------------------------------------------------
        // 開始処理の実行
        // --------------------------------------------------
        try{
            if(!this.initializeService()){
                return false;
            }
        }catch(LogicalException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Service initialization is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }
            throw e;
        }

        // --------------------------------------------------
        // 検索の実行
        // --------------------------------------------------
        APTemplateGeneralJDBCUtilDao dao = new APTemplateGeneralJDBCUtilDao();
        SimpleResultSet rs = null;
        try{
            // データベース接続
            dao.open();

            // データチェック
            boolean success = this.checkRecord(dao, condition);

            if(success){
                // SQLの設定
                dao.bindSQL(SQL_SELECT_TABLE);
                dao.clearParameters();
                dao.setParameter("COMPANYCD", this.getLoginModel().getCompanyCode());
                
//                dao.bindSQL(SQL_SELECT_CPANKENDAICHO);
//                dao.clearParameters();
//                dao.setParameter("COMPANYCD", this.getLoginModel().getCompanyCode());
//                dao.setParameter("ANKENNO", condition.ankenNo);
//                dao.setParameter("ANKENMEI", condition.ankenMei);
//                dao.setParameter("SHINCHOKUKBN", condition.shinchokuKbn);
//                dao.setParameter("JUCHUBI_FROM", condition.juchubiFrom);
//                dao.setParameter("JUCHUBI_TO", condition.juchubiTo);
//                dao.setParameter("EIGYOTANTOCD", condition.eigyoTantoCd);

                // SQLの実行
                rs = dao.executeQuery();
                while(rs.next()){
                    Detail2 detail = new Detail2();
                    detail.companyCode = rs.getString("COMPANYCD");
                    detail.menuId = rs.getString("MENUID");
                    detail.updDate = rs.getString("UPDDATE");
                    detail.menuKbn = rs.getString("MENUKBN");
                    detail.majorId = rs.getString("MAJORID");
                    detail.minorId = rs.getString("MINORID");
                    detail.screenId = rs.getString("SCREENID");
                    detail.menuNo = rs.getInt("MENUNO");
                    detail.menuNm = rs.getString("MENUNM");
                    detail.classNm = rs.getString("CLASSNM");
                    
//                    Detail detail = new Detail();
//                    detail.ankenNo = rs.getString("ANKENNO");
//                    detail.ankenKbn = rs.getString("ANKENKBN");
//                    detail.eigyoKbn = rs.getString("EIGYOKBN");
//                    detail.shinchokuKbn = rs.getString("SHINCHOKUKBN");
//                    detail.ankenMei = rs.getString("ANKENMEI");
//                    detail.eigyoTantoCd = rs.getString("EIGYOTANTOCD");
//                    detail.eigyoTantoMei = rs.getString("EIGYOTANTOMEI");
//                    detail.juchubi = rs.getString("JUCHUBI");
//                    detail.juchuKingaku = rs.getDouble("JUCHUKINGAKU");
//                    detail.revision = rs.getInt("REVISION");  
                    
                    details.add(detail);
                }

                // 該当データなしの場合
                int count = details.size();
                if(count <= 0){
                    // メッセージ表示(該当データなし)
                    this.setMessage("CMW00001");
                    // エラーログ出力(該当データなし)
                    if(LOGGER.isWarnEnabled()){
                        LOGGER.warn("CPANKENDAICHO data is not found.");
                    }
                    // 該当データ超過の場合
                }else if(count > MAX_ROWS){
                    // メッセージ表示(該当データ超過)
                    this.setMessage("CMW00002", NumberUtility.toDisplayFormat(MAX_ROWS));
                    // エラーログ出力(該当データ超過)
                    if(LOGGER.isWarnEnabled()){
                        LOGGER.warn("Selecting CPANKENDAICHO result exceeds " + MAX_ROWS + ".");
                    }
                    // 正常の場合
                }else{
                    // メッセージ表示(検索正常終了)
                    this.setMessage("CMN00004", NumberUtility.toDisplayFormat(count));
                    // ログ出力(検索正常終了)
                    if(LOGGER.isInfoEnabled()){
                        LOGGER.info("Selecting CPANKENDAICHO is success.");
                    }
                    ret = true;
                }
            }

        }catch(APTemplateSQLException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                // エラーログ出力(システムエラー)
                LOGGER.error("Selecting CPANKENDAICHO is failed. Some runtime exception happen on DB. SQL error code:" + e.getErrorCode());
            }
            throw e;
        }finally{
            // データベース切断
            try{
                rs.close();
            }catch(Exception e){
            }
            try{
                dao.close();
            }catch(Exception e){
            }
        }

        // --------------------------------------------------
        // 終了処理の実行
        // --------------------------------------------------
        this.finalizeService();

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 確定処理を実行する</p>
     * 
     * @param details (I)明細データ
     * @param removed (I)削除データ
     * @return 確定本処理の成否
     * @throws LogicalException 論理的例外
     */
    public boolean executeCommit(List<Detail2> details, List<Detail2> removed) throws LogicalException{

        boolean ret = false;

        // --------------------------------------------------
        // 開始処理の実行
        // --------------------------------------------------
        try{
            if(!this.initializeService()){
                return false;
            }
        }catch(LogicalException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Service initialization is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }
            throw e;
        }

        // --------------------------------------------------
        // 確定の実行
        // --------------------------------------------------
        APTemplateGeneralJDBCUtilDao dao = new APTemplateGeneralJDBCUtilDao();

        try{
            // データベース接続
            dao.open(false);

            boolean success = true;

            // データチェック
            for(Detail2 detail:details){
                if(!this.checkRecord(dao, detail)){
                    success = false;
                }
            }

            // 削除データの処理
            if(success){
                for(Detail2 detail:removed){
                    switch(detail.operation){
                        case OPERATIONMODE_DELETE:
                            success = this.deleteRecord(dao, detail);
                            break;
                    }
                    if(!success){
                        break;
                    }
                }
            }

            // 追加・更新データの処理
            if(success){
                for(Detail2 detail:details){
                    switch(detail.operation){
                        case OPERATIONMODE_INSERT:
                            success = this.insertRecord(dao, detail);
                            break;
                        case OPERATIONMODE_UPDATE:
                            success = this.updateRecord(dao, detail);
                            break;
                    }
                    if(!success){
                        break;
                    }
                }
            }

            // 実行結果の返却
            if(!success){
                // トランザクションの取消
                dao.rollback();

            }else{
                // トランザクションの確定
                dao.commit();

                // メッセージ表示(確定正常終了)
                this.setMessage("CMN00001");
                // ログ出力(確定正常終了)
                if(LOGGER.isInfoEnabled()){
                    LOGGER.info("Transaction on deleting CPANKENDAICHO and updating CPANKENDAICHO is success.");
                }
                ret = true;
            }

        }catch(APTemplateSQLException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                // エラーログ出力(システムエラー)
                LOGGER.error("Transaction on deleting CPANKENDAICHO and updating CPANKENDAICHO is failed. Some runtime exception happen on DB. SQL error code:" + e.getErrorCode());
            }
            throw e;
        }finally{
            // データベース切断
            try{
                dao.close();
            }catch(Exception e){
            }
        }

        // --------------------------------------------------
        // 終了処理の実行
        // --------------------------------------------------
        this.finalizeService();

        return ret;
    }

    // --------------------------------------------------------------------------
    // 内部メソッド
    // --------------------------------------------------------------------------

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : チェック処理を実行する</p>
     * 
     * @param dao データアクセスオブジェクト
     * @param condition 条件
     * @return F8(確定)押下時チェック結果の真偽
     * @throws LogicalException 論理的例外
     */
    protected boolean checkRecord(APTemplateGeneralJDBCUtilDao dao, Condition2 condition) throws LogicalException{

        boolean ret = false;
        boolean hasErrors = false;

        // --------------------------------------------------
        // コード存在チェックの実行
        // --------------------------------------------------

        try{

            // コード取得部品を生成
            CodeService service = new CodeService();
            service.setLoginModel(getLoginModel());
            service.setClientLocale(this.getClientLocale());
            // 営業担当
//            if(condition.eigyoTantoCd != null && !condition.eigyoTantoCd.equals("")){
//                if(!service.getUser(this.getLoginModel().getCompanyCode(), condition.eigyoTantoCd, null)){
//                    this.setMessage(hasErrors, "CME00024", "営業担当");
//                    condition.setFieldError(condition.EIGYO_TANTO_CD);
//                    hasErrors = true;
//                }
//            }

            if(!hasErrors){
                ret = true;
            }

//        }catch(LogicalException e){
//            // エラーログ出力(システムエラー)
//            if(LOGGER.isErrorEnabled()){
//                // エラーログ出力(システムエラー)
//                LOGGER.error("Selecting CCCODE is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
//            }
//            throw e;
//        }
        }finally{
            
        }
        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : チェック処理を実行する</p>
     * 
     * @param dao データアクセスオブジェクト
     * @param detail 明細データ
     * @return F8(確定)押下時チェック結果の真偽
     * @throws LogicalException 論理的例外
     */
    protected boolean checkRecord(APTemplateGeneralJDBCUtilDao dao, Detail2 detail) throws LogicalException{

        boolean ret = false;
        boolean hasErrors = false;

        // --------------------------------------------------
        // コード存在チェックの実行
        // --------------------------------------------------
        try{

            // コード取得部品を生成
            CodeService service = new CodeService();
            service.setLoginModel(getLoginModel());
            service.setClientLocale(this.getClientLocale());
//            // 営業担当
//            if(detail.eigyoTantoCd != null && !detail.eigyoTantoCd.equals("")){
//                if(!service.getUser(this.getLoginModel().getCompanyCode(), detail.eigyoTantoCd, null)){
//                    this.setMessage(hasErrors, "CME00024", "営業担当");
//                    detail.setFieldError(detail.EIGYO_TANTO_CD);
//                    hasErrors = true;
//                }
//            }

            if(!hasErrors){
                ret = true;
            }

//        }catch(LogicalException e){
//            // エラーログ出力(システムエラー)
//            if(LOGGER.isErrorEnabled()){
//                // エラーログ出力(システムエラー)
//                LOGGER.error("Search CCCODE is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
//            }
//            throw e;
//        }
        }finally{
            
        }

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 排他処理を実行する</p>
     * 
     * @param dao データアクセスオブジェクト
     * @param detail 明細データ
     * @return 排他処理の成否
     * @throws LogicalException 論理的例外
     */
    protected boolean lockRecord(APTemplateGeneralJDBCUtilDao dao, Detail2 detail) throws LogicalException{

        boolean ret = false;

        // --------------------------------------------------
        // 排他の実行
        // --------------------------------------------------

        SimpleResultSet rs = null;

        try{
            // SQLの設定
            dao.bindSQL(SQL_LOCK_TABLE);
            dao.clearParameters();
            dao.setParameter("COMPANYCD", detail.companyCode);
            dao.setParameter("MENUID", detail.menuId);
            dao.setParameter("REVISION", detail.revision);

//            dao.bindSQL(SQL_LOCK_CPANKENDAICHO);
//            dao.clearParameters();
//            dao.setParameter("COMPANYCD", this.getLoginModel().getCompanyCode());
//            dao.setParameter("ANKENNO", detail.ankenNo);
//            dao.setParameter("REVISION", detail.revision);

            // SQLの実行
            rs = dao.executeQuery();
            if(!rs.next()){
                // メッセージ返却(排他エラー)
                this.setMessage("CME00047");
                // エラーログ出力(排他エラー)
                if(LOGGER.isWarnEnabled()){
                    LOGGER.warn("Locking CPANKENDAICHO is failed. Lockable data is not found.");
                }
            }else{
                ret = true;
            }

        }catch(APTemplateSQLException e){

            if(e.getError() == APTemplateSQLException.RESOURCE_BUSY){
                // メッセージ返却(他端末にて更新中)
                this.setMessage("CME00046");
                // エラーログ出力(他端末にて更新中)
                if(LOGGER.isWarnEnabled()){
                    LOGGER.warn("Locking CPANKENDAICHO is failed. The table is already locked. SQL error code. " + e.getError());
                }
            }else{
                // エラーログ出力(システムエラー)
                if(LOGGER.isErrorEnabled()){
                    LOGGER.error("Locking CPANKENDAICHO is failed. Some runtime exception happen on DB. SQL error code. " + e.getErrorCode());
                }
                throw e;
            }

        }finally{
            try{
                rs.close();
            }catch(Exception e){
            }
        }

        if(!ret){
            // 項目エラーを設定
            detail.setFieldError(detail.COMPANYCD);
            detail.setFieldError(detail.MENUID);
//            detail.setFieldError(detail.ANKEN_NO);
        }

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 追加処理を実行する</p>
     * 
     * @param dao データアクセスオブジェクト
     * @param detail 明細データ
     * @return 追加本処理の成否
     * @throws LogicalException 論理的例外
     */
    protected boolean insertRecord(APTemplateGeneralJDBCUtilDao dao, Detail2 detail) throws LogicalException{

        boolean ret = false;

        // --------------------------------------------------
        // 登録の実行
        // --------------------------------------------------
        try{
            // SQLの設定
            dao.bindSQL(SQL_INSERT_CPANKENDAICHO);
            dao.clearParameters();
            dao.setParameter("COMPANYCD", detail.companyCode);
            dao.setParameter("MENUID", detail.menuId);
            
//            dao.bindSQL(SQL_INSERT_CPANKENDAICHO);
//            dao.clearParameters();
//            dao.setParameter("COMPANYCD", this.getLoginModel().getCompanyCode());
//            dao.setParameter("ANKENNO", detail.ankenNo);
//            dao.setParameter("INSUSRID", this.getLoginModel().getUserId());
//            dao.setParameter("INSPGID", this.getProgramId());
//            dao.setParameter("UPDUSRID", this.getLoginModel().getUserId());
//            dao.setParameter("UPDPGID", this.getProgramId());
//            dao.setParameter("ANKENKBN", detail.ankenKbn);
//            dao.setParameter("EIGYOKBN", detail.eigyoKbn);
//            dao.setParameter("SHINCHOKUKBN", detail.shinchokuKbn);
//            dao.setParameter("ANKENMEI", detail.ankenMei);
//            dao.setParameter("JUCHUBI", detail.juchubi);
//            dao.setParameter("EIGYOBUMONCD", this.getDivision(detail.eigyoTantoCd));
//            dao.setParameter("EIGYOTANTOCD", detail.eigyoTantoCd);
//            dao.setParameter("JUCHUKINGAKU", detail.juchuKingaku);

            // SQLの実行
            int count = dao.executeUpdate();
            if(count <= 0){
                // メッセージ返却(登録失敗)
                this.setMessage("CME00034", MessageManager.getMessage(messageFileClassPath, this.getClientLocale(), "message.insert"));
                // エラーログ出力(登録失敗)
                if(LOGGER.isWarnEnabled()){
                    LOGGER.warn("Inserting CPANKENDAICHO is failed. No data is inserted.");
                }
            }else{
                ret = true;
            }

        }catch(LogicalException e){

            if(!(e instanceof APTemplateSQLException)){
                // エラーログ出力(システムエラー)
                if(LOGGER.isErrorEnabled()){
                    LOGGER.error("Inserting CPANKENDAICHO is failed. The data already exists. SQL error code:" + ((APTemplateSQLException)e).getError());
                }
                throw e;
            }

            if(((APTemplateSQLException)e).getError() == APTemplateSQLException.UNIQUE_CONSTRAINT_VIOLATED){

                // メッセージ返却(データ登録済), "message.data:当該データ"
                this.setMessage("CME00022", MessageManager.getMessage(messageFileClassPath, this.getClientLocale(), "message.data"));
                // エラーログ出力(データ登録済)
                if(LOGGER.isErrorEnabled()){
                    LOGGER.error("Inserting CPANKENDAICHO is failed. Some runtime exception happen on DB. SQL error code:" + ((APTemplateSQLException)e).getErrorCode());
                }

            }else{

                // エラーログ出力(システムエラー)
                if(LOGGER.isErrorEnabled()){
                    LOGGER.error("Inserting CPANKENDAICHO is failed. Some runtime exception happen. Please check stacktrace" + e.getMessage());
                }
                throw e;

            }
        }

        if(!ret){
            // 項目エラーを設定
            detail.setFieldError(detail.COMPANYCD);
            detail.setFieldError(detail.MENUID);
//            detail.setFieldError(detail.ANKEN_NO);
        }

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 更新処理を実行する</p>
     * 
     * @param dao データアクセスオブジェクト
     * @param detail 明細データ
     * @return 更新本処理の成否
     * @throws LogicalException 論理的例外
     */
    protected boolean updateRecord(APTemplateGeneralJDBCUtilDao dao, Detail2 detail) throws LogicalException{

        boolean ret = false;

        // --------------------------------------------------
        // 排他の実行
        // --------------------------------------------------
        try{
            if(!this.lockRecord(dao, detail)){
                return false;
            }
        }catch(LogicalException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Locking CPANLENDAICHO is failed. Some runtime exception happen on DB. Please check stacktrace. " + e.getMessage());
            }
            throw e;
        }

        // --------------------------------------------------
        // 更新の実行
        // --------------------------------------------------
        try{

            // SQLの設定
            dao.bindSQL(SQL_UPDATE_TABLE);
            dao.setParameter("UPDUSRID", this.getLoginModel().getUserId());
            dao.setParameter("UPDPGID", this.getProgramId());
            dao.setParameter("COMPANYCD", detail.companyCode);
            dao.setParameter("MENUID", detail.menuId);
            dao.setParameter("MENUKBN", detail.menuKbn);
            dao.setParameter("MAJORID", detail.majorId);
            dao.setParameter("MINORID", detail.minorId);
            dao.setParameter("SCREENID", detail.screenId);
            dao.setParameter("MENUNO", detail.menuNo);
            dao.setParameter("MENUNM", detail.menuNm);
            dao.setParameter("CLASSNM", detail.classNm);
            

//            dao.bindSQL(SQL_UPDATE_CPANKENDAICHO);
//            dao.setParameter("UPDUSRID", this.getLoginModel().getUserId());
//            dao.setParameter("UPDPGID", this.getProgramId());
//            dao.setParameter("ANKENMEI", detail.ankenMei);
//            dao.setParameter("ANKENKBN", detail.ankenKbn);
//            dao.setParameter("EIGYOKBN", detail.eigyoKbn);
//            dao.setParameter("SHINCHOKUKBN", detail.shinchokuKbn);
//            dao.setParameter("EIGYOBUMONCD", this.getDivision(detail.eigyoTantoCd));
//            dao.setParameter("EIGYOTANTOCD", detail.eigyoTantoCd);
//            dao.setParameter("JUCHUBI", detail.juchubi);
//            dao.setParameter("JUCHUKINGAKU", detail.juchuKingaku);
//            dao.setParameter("COMPANYCD", this.getLoginModel().getCompanyCode());
//            dao.setParameter("ANKENNO", detail.ankenNo);

            // SQLの実行
            int count = dao.executeUpdate();
            if(count <= 0){
                // メッセージ返却(更新失敗)
                this.setMessage("CME00034", MessageManager.getMessage(messageFileClassPath, this.getClientLocale(), "message.update"));
                // エラーログ出力(更新失敗)
                if(LOGGER.isWarnEnabled()){
                    LOGGER.warn("Updating CPANKENDAICHO is failed. No data is updated.");
                }
            }else{
                ret = true;
            }

        }catch(APTemplateSQLException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Updating CPANKENDAICHO is failed. Some runtime exception happen on DB. SQL error code:" + e.getErrorCode());
            }
            throw e;
        }

        if(!ret){
            // 項目エラーを設定
            detail.setFieldError(detail.COMPANYCD);
            detail.setFieldError(detail.MENUID);
//            detail.setFieldError(detail.ANKEN_NO);
        }

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 削除処理を実行する</p>
     * 
     * @param dao データアクセスオブジェクト
     * @param detail 明細データ
     * @return 削除本処理の成否
     * @throws LogicalException 論理的例外
     */
    protected boolean deleteRecord(APTemplateGeneralJDBCUtilDao dao, Detail2 detail) throws LogicalException{

        boolean ret = false;

        // --------------------------------------------------
        // 排他の実行
        // --------------------------------------------------

        try{

            if(!this.lockRecord(dao, detail)){
                return false;
            }

        }catch(LogicalException e){

            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Locking CPANKENDAICHO is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }

            throw e;
        }
        // --------------------------------------------------
        // 削除の実行
        // --------------------------------------------------
        try{

            // SQLの設定
            dao.bindSQL(SQL_DELETE_TABLE);
            dao.setParameter("COMPANYCD", detail.companyCode);
            dao.setParameter("ANKENNO", detail.menuId);

//            dao.bindSQL(SQL_DELETE_CPANKENDAICHO);
//            dao.setParameter("COMPANYCD", this.getLoginModel().getCompanyCode());
//            dao.setParameter("ANKENNO", detail.ankenNo);

            // SQLの実行
            int count = dao.executeUpdate();

            if(count <= 0){
                // メッセージ返却(削除失敗)
                this.setMessage("CME00034", MessageManager.getMessage(messageFileClassPath, this.getClientLocale(), "message.update"));
                // エラーログ出力(削除失敗)
                if(LOGGER.isWarnEnabled()){
                    LOGGER.warn("Updating CPANKENDAICHO is failed. No data is updated.");
                }

            }else{
                ret = true;
            }

        }catch(APTemplateSQLException e){

            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Updating CPANKENDAICHO is failed. Some runtime exception happen on DB. SQL error code:" + e.getErrorCode());
            }

            throw e;
        }

        if(!ret){
            // 項目エラーを設定
            detail.setFieldError(detail.COMPANYCD);
            detail.setFieldError(detail.MENUID);
//            detail.setFieldError(detail.ANKEN_NO);
        }

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 部門名称取得処理を実行する</p>
     * 
     * @param userId ユーザID
     * @return 部門名称
     * @throws LogicalException 論理的例外
     */
    protected String getDivision(String userId) throws LogicalException{

        // ユーザIDが入力されていない場合は処理しない
        if(userId == null || "".equals(userId)){
            return "";
        }

        // --------------------------------------------------
        // 検索の実行
        // --------------------------------------------------

        try{

            // サービスの生成
            CodeService service = new CodeService();
            service.setLoginModel(getLoginModel());
            service.setClientLocale(this.getClientLocale());

            // 引数の設定
            CodeService.UserModel model = service.new UserModel();

            // サービスの実行
            boolean success = service.getUser(this.getLoginModel().getCompanyCode(), userId, model);

            // 実行結果の取得
            if(success){
                return model.divisionCd;
            }

            return "";

        }catch(LogicalException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Search Division is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }
            throw e;
        }

    }

}
