/*------------------------------------------------------------------------------
 * PROGNER EX for Java Servlet
 * Copyright(C) 2008 - 2014 Canon IT Solutions Inc. All rights reserved.
 *----------------------------------------------------------------------------*/
package jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2060.page;

import java.util.ArrayList;
import java.util.List;

import jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2060.service.CC2060S02;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2060.service.CC2060S02Mockup;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.auditlog.AuditLogger;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.component.APTemplateExListView;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.dto.LoginModel;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.page.BasePage;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.session.AppSession;
import jp.co.canonits.prognerex.core.common.dto.BaseDto;
import jp.co.canonits.prognerex.core.common.exception.LogicalException;
import jp.co.canonits.prognerex.core.common.utility.DateUtility;
import jp.co.canonits.prognerex.core.common.validator.PrognerRegExpJavaPattern;
import jp.co.canonits.prognerex.core.presentation_wicket.behavior.ExAjaxBehavior;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExCheckBox;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExDatePicker;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExDropDownChoice;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExDropDownItem;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExFieldSet;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExLabel;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExPagingNavigator;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExTextField;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>汎用マスタメンテナンス</p>
 * 
 * @author Canon IT Solutions Inc. R&amp;D Center
 * @version 2.3
 */
public class CC2060C02 extends BasePage{

    /**
     * ログインスタンス
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CC2060C02.class);

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;

    // ------------------------------------------------------
    // ページパラメータ
    // ------------------------------------------------------

    public static final String PARAM_SCHEMA_NAME = "SCHEMANM";
    public static final String PARAM_TABLE_NAME = "TABLENM";

    // --------------------------------------------------------------------------
    // 内部定数
    // --------------------------------------------------------------------------
    
    private static final String APPENDIX_TYPE_STRING = "　［文字］";
    private static final String APPENDIX_TYPE_NUMBER = "　［数値］";
    
    public static final String SEARCH_PREFIX = "PREFIX";
    public static final String SEARCH_SUFFIX = "SUFFIX";
    public static final String SEARCH_PARTIAL = "PARTIAL";
    public static final String SEARCH_COMPLETE = "COMPLETE";

    public static final String SEARCH_ABOVE = "ABOVE";
    public static final String SEARCH_BELOW = "BELOW";
    public static final String SEARCH_EQUAL = "EQUAL";

    /**
     * 明細テーブル表示件数
     */
    protected final int ROWS_PER_PAGE = 10;

    // --------------------------------------------------------------------------
    // 内部変数
    // --------------------------------------------------------------------------
    
    private String schemaName;
    private String tableName;

    // --------------------------------------------------
    // 条件部
    // --------------------------------------------------

    /**
     * 条件部パネル
     */
    protected ExFieldSet pnlCondition;
    
    protected ExDropDownChoice<ExDropDownItem> lstItemName;
    protected ExTextField<String> txtValue;
    protected ExDropDownChoice<ExDropDownItem> lstCondition;
    protected ExTextField<String> txtUpdFrom;
    protected ExTextField<String> txtUpdTo;

    // --------------------------------------------------
    // 明細部
    // --------------------------------------------------

    /**
     * 明細部パネル
     */
    protected ExFieldSet pnlDetail;

    /**
     * 明細テーブル
     */
    protected APTemplateExListView<Record2> tblDetail;

    /**
     * 明細テーブル(削除済)
     */
    protected List<Record2> tblRemoved;
    
    
    protected class Record2 extends BaseDto{

        /**
         * シリアルバージョンUID
         */
        private static final long serialVersionUID = 1L;

        /**
         * 確定
         */
        protected ExCheckBox chkCommit;

        /**
         * 削除
         */
        protected ExCheckBox chkDelete;
        
        protected ExTextField<String> txtCompanyCode;
        protected ExTextField<String> txtMenuId;
        protected ExTextField<String> txtUpdDate;
        protected ExTextField<String> txtMenuKbn;
        protected ExTextField<String> txtMajorId;
        protected ExTextField<String> txtMinorId;
        protected ExTextField<String> txtScreenId;
        protected ExTextField<String> txtMenuNo;
        protected ExTextField<String> txtMenuNm;
        protected ExTextField<String> txtClassNm;
       
        /**
         * リビジョン
         */
        protected int revision;

        /**
         * 処理モード
         */
        protected int operation;
    }

    // --------------------------------------------------
    // リスト要素
    // --------------------------------------------------
    
    protected List<ExDropDownItem> lstItemNameChoices;
    protected List<ExDropDownItem> lstStringConditionChoices;
    protected List<ExDropDownItem> lstNumberConditionChoices;

    // --------------------------------------------------
    // ダイアログ
    // --------------------------------------------------

    // --------------------------------------------------------------------------
    // 公開メソッド
    // --------------------------------------------------------------------------

    /**
     * <p>起動条件 : コンストラクタ</p>
     * <p>処理概要 : 画面レイアウトを生成する</p>
     */
    public CC2060C02(){

        // --------------------------------------------------
        // プログラム情報
        // --------------------------------------------------
        // 処理ID
        setOperationId("CC2060");
        // プログラムID
        setProgramId("CC2060C01");
        // プログラム名
        setProgramName("汎用マスタメンテナンス");
        // プログラムリビジョン
        setProgramRevision("01.00");

        // --------------------------------------------------
        // アプリケーション生成
        // --------------------------------------------------
        // 画面タイトルを設定
        this.setTitle();
        // 画面コンポーネントを生成
        this.initializeComponents();
        // 初期化処理を実行
        this.initializeEvent();
    }
    
    public CC2060C02(PageParameters params){

        this();

        if(params != null){
            this.schemaName = params.getString(PARAM_SCHEMA_NAME);
            this.tableName = params.getString(PARAM_TABLE_NAME);

            // 検索イベントを実施
            this.functionEvent05();
        }
        
    }

    /**
     * <p>起動条件 : サービス呼出し時</p>
     * <p>処理概要 : サービスを生成し返却する</p>
     * 
     * @return サービス
     */
    protected CC2060S02 getService(){
        CC2060S02 service = null;
        AppSession session = (AppSession)this.getSession();
        LoginModel loginModel = session.getLoginModel();
        if(this.isMockupMode()){
            service = new CC2060S02Mockup();
        }else{
            service = new CC2060S02();
        }
        service.setLoginModel(loginModel);
        service.setClientLocale(this.getLocale());
        return service;
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 画面コンポーネントを生成する</p>
     * 
     */
    protected void initializeComponents(){

        // --------------------------------------------------
        // 条件部の生成
        // --------------------------------------------------

        // 条件部パネル
        this.pnlCondition = new ExFieldSet("pnlCondition", "lblCondition", "条件部");
        this.getForm().add(this.pnlCondition);

        // 項目名
        this.lstItemName = new ExDropDownChoice<ExDropDownItem>("lstItemName", new Model<ExDropDownItem>());
        this.pnlCondition.add(this.lstItemName);

        this.lstItemName.add(new ExAjaxBehavior(this.getForm(), ExAjaxBehavior.EVENT_ONCHANGE){
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target){
                lstItemNameChange(this.getComponent(), target);
            }
        });
        
        // 値
        this.txtValue = new ExTextField<String>("txtValue", new Model<String>());
        this.pnlCondition.add(this.txtValue);

        // 条件
        this.lstCondition = new ExDropDownChoice<ExDropDownItem>("lstCondition", new Model<ExDropDownItem>());
        this.pnlCondition.add(this.lstCondition);
        
        // 受注日(FROM)
        this.txtUpdFrom = new ExTextField<String>("txtUpdFrom", new Model<String>());
        this.pnlCondition.add(this.txtUpdFrom);
        this.txtUpdFrom.setClientDateValidator(ExTextField.DATE_DISP_FORMAT_YMD);
        this.txtUpdFrom.add(new ExDatePicker());

        // 受注日(TO)
        this.txtUpdTo = new ExTextField<String>("txtUpdTo", new Model<String>());
        this.pnlCondition.add(this.txtUpdTo);
        this.txtUpdTo.setClientDateValidator(ExTextField.DATE_DISP_FORMAT_YMD);
        this.txtUpdTo.add(new ExDatePicker());

        // パネルを使用不可に設定
//        this.setContainerEnabled(this.pnlCondition, false);

        // --------------------------------------------------
        // 明細部の生成
        // --------------------------------------------------

        // 明細部パネル
        this.pnlDetail = new ExFieldSet("pnlDetail", "lblDetail", "明細部");
        this.getForm().add(this.pnlDetail);

        // 明細テーブル
        this.tblDetail = new APTemplateExListView<Record2>("tblDetail", new ArrayList<Record2>(), ROWS_PER_PAGE){
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Record2> item){
                populateListItem(item);
            }
        };
        this.pnlDetail.add(this.tblDetail);

        // 明細ナビゲータ
        this.pnlDetail.add(new ExPagingNavigator("navigator", this.tblDetail));

        // パネルを使用不可に設定
        this.setContainerEnabled(this.pnlDetail, false);

        // --------------------------------------------------
        // ファンクション部のラベル設定
        // --------------------------------------------------

        // [F10]
        this.setFunction10Label("");
        // [F12]
        this.setFunction12Label("");

        // --------------------------------------------------
        // ファンクション部のスクリプト設定
        // --------------------------------------------------

        // [F04]選択
        this.setFunction04Script(this.getSelectCurrentRowScript("chkDelete"));
        // [F08]確定
        this.setFunction08Script(this.getConfirmDialogScript("CMI00007"));

        // --------------------------------------------------
        // ファンクション部のAJAXイベント設定
        // --------------------------------------------------

        // --------------------------------------------------
        // ファンクション部のダイアログ設定
        // --------------------------------------------------

    }

    /**
     * <p>起動条件 : 明細テーブル表示時</p>
     * <p>処理概要 : 明細レコードを生成する</p>
     * 
     * @param item 明細行
     */
    protected void populateListItem(ListItem<Record2> item){

        final Record2 record = (Record2)item.getModelObject();

        // ハイライト表示
        this.tblDetail.setHilight(item);

        // 行番号
        item.add(new ExLabel("lblIndex", String.valueOf(item.getIndex() + 1)));

        // 確定
        item.add(record.chkCommit.setMoveRowOnKeyDownScript());

        // 削除
        item.add(record.chkDelete.setMoveRowOnKeyDownScript());
        
        record.txtCompanyCode.setEnabled(record.operation == OPERATIONMODE_INSERT ? true : false);
        item.add(record.txtCompanyCode.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        
        record.txtMenuId.setEnabled(record.operation == OPERATIONMODE_INSERT ? true : false);
        item.add(record.txtMenuId.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        
        record.txtUpdDate.setEnabled(false);
        item.add(record.txtUpdDate.setMoveRowOnKeyDownScript());
        
        item.add(record.txtMenuKbn.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        item.add(record.txtMajorId.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        item.add(record.txtMinorId.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        item.add(record.txtScreenId.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        item.add(record.txtMenuNo.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        item.add(record.txtMenuNm.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
        item.add(record.txtClassNm.setChecker(record.chkCommit).setMoveRowOnKeyDownScript()); 
        
    }

    /**
     * <p>先頭項目にフォーカスを設定する</p>
     * 
     */
    protected void setFocusToFirstItem(){

        // 条件部
        if(this.pnlCondition.isEnabled()){
            this.setFocus(this.lstItemName);
            // 明細部
        }else{
            if(this.tblDetail.getModelObject().size() > 0){
                if(operationMode == OPERATIONMODE_INSERT){
                    this.setFocus(this.tblDetail.getModelObject().get(0).txtCompanyCode);
                }else{
                    this.setFocus(this.tblDetail.getModelObject().get(0).txtMenuKbn);
                }
            }
        }

    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return 初期処理の成否
     */
    @Override
    protected boolean executeInit(){
        boolean ret = false;

        // --------------------------------------------------
        // 初期処理の実行
        // --------------------------------------------------

        try{

            // サービスの取得
            CC2060S02 service = this.getService();

            // 引数(OUT)の設定
            CC2060S02.Choices choices = service.new Choices();

            // サービスの実行
            ret = service.executeInit(choices);

            // 実行結果の表示
            if(!ret){
                this.showMessage(service.getMessageModel());

            }else{

                // --------------------------------------------------
                // リストボックスの初期設定
                // --------------------------------------------------
                
                // 対象項目
                this.lstItemNameChoices = new ArrayList<ExDropDownItem>();
                this.lstItemNameChoices.add(new ExDropDownItem("",""));
                this.lstItemNameChoices.add(new ExDropDownItem("COMPANYCD","会社コード"+APPENDIX_TYPE_STRING));
                this.lstItemNameChoices.add(new ExDropDownItem("MENUID","メニューID"+APPENDIX_TYPE_STRING));
                this.lstItemNameChoices.add(new ExDropDownItem("MENUKBN","メニュー区分"+APPENDIX_TYPE_STRING));
                this.lstItemNameChoices.add(new ExDropDownItem("MAJORID","大分類ID"+APPENDIX_TYPE_STRING));
                this.lstItemNameChoices.add(new ExDropDownItem("MINORID","小分類ID"+APPENDIX_TYPE_STRING));
                this.lstItemNameChoices.add(new ExDropDownItem("SCREENID","画面ID"+APPENDIX_TYPE_STRING));
                this.lstItemNameChoices.add(new ExDropDownItem("MENUNO","メニュー項番"+APPENDIX_TYPE_NUMBER));
                this.lstItemNameChoices.add(new ExDropDownItem("MENUNM","メニュー名"+APPENDIX_TYPE_STRING));
                this.lstItemNameChoices.add(new ExDropDownItem("CLASSNM","クラス名"+APPENDIX_TYPE_STRING));
                
                // 条件（文字）
                this.lstStringConditionChoices = new ArrayList<ExDropDownItem>();
                this.lstStringConditionChoices.add(new ExDropDownItem("",""));
                this.lstStringConditionChoices.add(new ExDropDownItem(SEARCH_PREFIX,"前方一致"));
                this.lstStringConditionChoices.add(new ExDropDownItem(SEARCH_SUFFIX,"後方一致"));
                this.lstStringConditionChoices.add(new ExDropDownItem(SEARCH_PARTIAL,"部分一致"));
                this.lstStringConditionChoices.add(new ExDropDownItem(SEARCH_COMPLETE,"完全一致"));
                
                // 条件（数値）
                this.lstNumberConditionChoices = new ArrayList<ExDropDownItem>();
                this.lstNumberConditionChoices.add(new ExDropDownItem("",""));
                this.lstNumberConditionChoices.add(new ExDropDownItem(SEARCH_ABOVE,"以上"));
                this.lstNumberConditionChoices.add(new ExDropDownItem(SEARCH_BELOW,"以下"));
                this.lstNumberConditionChoices.add(new ExDropDownItem(SEARCH_EQUAL,"等しい"));

                // --------------------------------------------------
                // 初期値の設定
                // --------------------------------------------------
                
                // 対象項目
                this.lstItemName.setChoices(this.lstItemNameChoices);
            }

        }catch(LogicalException e){

            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Master Maintenance Search service initialization is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }
            // スタックトレース出力(システムエラー)
            this.writeStackTrace(e);
            throw new IllegalStateException(e);

        }

        return ret;
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return 初期処理における後処理の成否
     */
    @Override
    protected boolean postInit(){

        // --------------------------------------------------
        // 条件部
        // --------------------------------------------------
        this.setContainerEnabled(this.pnlCondition, true);

        // --------------------------------------------------
        // 明細部
        // --------------------------------------------------

        // --------------------------------------------------
        // 処理モード
        // --------------------------------------------------

        // --------------------------------------------------
        // ファンクション部
        // --------------------------------------------------

        // [F02]メニュー
        this.setFunction02Enabled(true);
        // [F03]クリア
        this.setFunction03Enabled(true);
        // [F05]検索
        this.setFunction05Enabled(true);
        // [F06]追加
        this.setFunction06Enabled(true);

        // --------------------------------------------------
        // フォーカス設定
        // --------------------------------------------------

        this.setFocusToFirstItem();

        return true;
    }

    /**
     * <p>起動条件 : F3(クリア)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return クリア本処理の成否
     */
    @Override
    protected boolean executeClear(){
        
        boolean ret = false;

        // --------------------------------------------------
        // 条件部
        // --------------------------------------------------

        // エラー表示のクリア
        this.clearContainerError(this.pnlCondition);
        
        // 入力値のクリア
//        this.lstItemName.setIndex(-1);
//        this.txtValue.clearInput();
//        this.lstCondition.setIndex(-1);

        // --------------------------------------------------
        // 明細部
        // --------------------------------------------------

        // エラー表示のクリア
        this.clearContainerError(this.pnlDetail);

        // 入力内容のクリア
        this.tblDetail.setModelObject(new ArrayList<Record2>());
        this.tblRemoved = null;

        // --------------------------------------------------
        // 検索の実行
        // --------------------------------------------------
        
        ret = this.executeSearch();
        if(ret){
            ret = this.postSearch();
        }

        return true;
    }

    /**
     * <p>起動条件 : F3(クリア)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return クリア処理における後処理の成否
     */
    @Override
    protected boolean postClear(){

        // --------------------------------------------------
        // 条件部
        // --------------------------------------------------

        this.setContainerEnabled(this.pnlCondition, true);

        // --------------------------------------------------
        // 明細部
        // --------------------------------------------------

        this.setContainerEnabled(this.pnlDetail, false);

        // --------------------------------------------------
        // 処理モード
        // --------------------------------------------------

        this.operationMode = OPERATIONMODE_INIT;

        // --------------------------------------------------
        // ファンクション部
        // --------------------------------------------------

        // [F04]選択
        this.setFunction04Enabled(false);
        // [F05]検索
        this.setFunction05Enabled(true);
        // [F06]追加
        this.setFunction06Enabled(true);
        // [F07]削除
        this.setFunction07Enabled(false);
        // [F08]確定
        this.setFunction08Enabled(false);

        // --------------------------------------------------
        // フォーカス設定
        // --------------------------------------------------

        this.setFocusToFirstItem();

        return true;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return 検索処理における前処理の成否
     */
    @Override
    protected boolean preSearch(){

        boolean ret = false;
        boolean hasErrors = false;

        // --------------------------------------------------
        // エラーのクリア
        // --------------------------------------------------

//        this.clearContainerError(this.pnlCondition);
        this.executeClear();

        // --------------------------------------------------
        // 条件部の入力チェック
        // --------------------------------------------------
        
        // 対象項目が選択されている場合のみ検証を行う
//        if (this.lstItemName.getModelValue() != "-1") {
//            
//            // 値
//            if (!this.checkTextRequired(hasErrors, this.txtValue, "値")) {
//                hasErrors = true;
//            }
//            
//            // 条件
//            if (!this.checkTextRequired(hasErrors, this.lstCondition, "条件")) {
//                hasErrors = true;
//            }
//        }

      // 更新日(FROM)
      // null値でなく空文字でなければ検証を行う
      if(this.txtUpdFrom.getValue() != null && !"".equals(this.txtUpdFrom.getValue())){
          if(!this.checkTextDate(hasErrors, this.txtUpdFrom, "更新日(FROM)", DateUtility.DISP_FORMAT_YMD)){
              hasErrors = true;
          }
      }

      // 更新日(TO)
      if(this.txtUpdTo.getValue() != null && !"".equals(this.txtUpdTo.getValue())){
          if(!this.checkTextDate(hasErrors, this.txtUpdTo, "更新日(TO)", DateUtility.DISP_FORMAT_YMD)){
              hasErrors = true;
          }
      }

//        // null値でなく空文字でなければ検証を行う
//        if(this.txtAnkenNo.getValue() != null && !"".equals(this.txtAnkenNo.getValue())){
//            if(!this.checkTextLength(hasErrors, this.txtAnkenNo, "案件NO", 3, 6)){
//                hasErrors = true;
//            }else if(!this.checkTextCharactor(hasErrors, this.txtAnkenNo, "案件NO", PrognerRegExpJavaPattern.CHARSET_NUMERIC)){
//                hasErrors = true;
//            }
//        }
//
//        // 受注日(FROM)
//        // null値でなく空文字でなければ検証を行う
//        if(this.txtJuchubiFrom.getValue() != null && !"".equals(this.txtJuchubiFrom.getValue())){
//            if(!this.checkTextDate(hasErrors, this.txtJuchubiFrom, "受注日", DateUtility.DISP_FORMAT_YMD)){
//                hasErrors = true;
//            }
//        }
//
//        // 受注日(TO)
//        if(this.txtJuchubiTo.getValue() != null && !"".equals(this.txtJuchubiTo.getValue())){
//            if(!this.checkTextDate(hasErrors, this.txtJuchubiTo, "受注日", DateUtility.DISP_FORMAT_YMD)){
//                hasErrors = true;
//            }
//        }
//
//        // 営業担当コード
//        if(this.txtEigyoTantoCd.getValue() != null && !"".equals(this.txtEigyoTantoCd.getValue())){
//            if(!this.checkTextLength(hasErrors, this.txtEigyoTantoCd, "営業担当コード", 1, 10)){
//                hasErrors = true;
//            }
//        }
//
//        // 案件名
//        if(this.txtAnkenMei.getValue() != null && !"".equals(this.txtAnkenMei.getValue())){
//            if(!this.checkTextCodePoint(hasErrors, this.txtAnkenMei, "案件名")){
//                hasErrors = true;
//            }else if(!this.checkTextLength(hasErrors, this.txtAnkenMei, "案件名", 1, 30)){
//                hasErrors = true;
//            }
//        }

        // --------------------------------------------------
        // 条件部の項目間チェック
        // --------------------------------------------------

        // 更新日(FROM-TO)
        if(!hasErrors){
            String juchubiFrom = this.txtUpdFrom.getValue();
            juchubiFrom = (juchubiFrom == null || "".equals(juchubiFrom)) ? juchubiFrom : DateUtility.toValueFormat(this.txtUpdFrom.getModelObject(), DateUtility.DISP_FORMAT_YMD);

            String juchubiTo = this.txtUpdTo.getValue();
            juchubiTo = (juchubiTo == null || "".equals(juchubiTo)) ? juchubiTo : DateUtility.toValueFormat(this.txtUpdTo.getModelObject(), DateUtility.DISP_FORMAT_YMD);

            if(juchubiFrom != null && juchubiTo != null && juchubiFrom.compareTo(juchubiTo) > 0){
                hasErrors = true;
                this.setFieldError(this.txtUpdFrom);
                this.setFieldError(this.txtUpdTo);
                this.showMessage("CME00015", "受注日");
                this.setFocus(this.txtUpdFrom);
            }
        }

//        // 受注日(FROM-TO)
//        if(!hasErrors){
//            String juchubiFrom = this.txtJuchubiFrom.getValue();
//            juchubiFrom = (juchubiFrom == null || "".equals(juchubiFrom)) ? juchubiFrom : DateUtility.toValueFormat(this.txtJuchubiFrom.getModelObject(), DateUtility.DISP_FORMAT_YMD);
//
//            String juchubiTo = this.txtJuchubiTo.getValue();
//            juchubiTo = (juchubiTo == null || "".equals(juchubiTo)) ? juchubiTo : DateUtility.toValueFormat(this.txtJuchubiTo.getModelObject(), DateUtility.DISP_FORMAT_YMD);
//
//            if(juchubiFrom != null && juchubiTo != null && juchubiFrom.compareTo(juchubiTo) > 0){
//                hasErrors = true;
//                this.setFieldError(this.txtJuchubiFrom);
//                this.setFieldError(this.txtJuchubiTo);
//                this.showMessage("CME00015", "受注日");
//                this.setFocus(this.txtJuchubiFrom);
//            }
//        }

        // --------------------------------------------------
        // エラーの判定
        // --------------------------------------------------

        if(!hasErrors){
            ret = true;
        }

        return ret;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return 検索本処理の成否
     */
    @Override
    protected boolean executeSearch(){

        boolean ret = false;

        // --------------------------------------------------
        // 検索の実行
        // --------------------------------------------------

        try{
            // サービスの取得
            CC2060S02 service = this.getService();

            // 引数(IN)の設定
            CC2060S02.Condition2 condition = service.new Condition2();
            condition.itemName = this.lstItemName.getValue();
            condition.itemValue = this.txtValue.getValue();
            condition.itemCond = this.lstCondition.getValue();

            String updFrom = this.txtUpdFrom.getValue();
            condition.updFrom = (updFrom == null || "".equals(updFrom)) ? updFrom : DateUtility.toValueFormat(this.txtUpdFrom.getModelObject(), DateUtility.DISP_FORMAT_YMD);

            String updTo = this.txtUpdTo.getValue();
            condition.updTo = (updTo == null || "".equals(updTo)) ? updTo : DateUtility.toValueFormat(this.txtUpdTo.getModelObject(), DateUtility.DISP_FORMAT_YMD);
            
//            condition.ankenNo = this.txtAnkenNo.getModelObject();
//            condition.ankenMei = this.txtAnkenMei.getModelObject();
//            condition.shinchokuKbn = this.lstShinchokuKbn.getModelObject().getId();
//            condition.eigyoTantoCd = this.txtEigyoTantoCd.getModelObject();
//            String juchubiFrom = this.txtJuchubiFrom.getValue();
//            condition.juchubiFrom = (juchubiFrom == null || "".equals(juchubiFrom)) ? juchubiFrom : DateUtility.toValueFormat(this.txtJuchubiFrom.getModelObject(), DateUtility.DISP_FORMAT_YMD);
//
//            String juchubiTo = this.txtJuchubiTo.getValue();
//            condition.juchubiTo = (juchubiTo == null || "".equals(juchubiTo)) ? juchubiTo : DateUtility.toValueFormat(this.txtJuchubiTo.getModelObject(), DateUtility.DISP_FORMAT_YMD);

            // 引数(OUT)の設定
            List<CC2060S02.Detail2> details = new ArrayList<CC2060S02.Detail2>();

            // サービスの実行
            ret = service.executeSearch(condition, details);

            // 実行結果を表示(確定後の再検索時には表示しない)
            if(this.operationMode == OPERATIONMODE_INIT){
                this.showMessage(service.getMessageModel());
            }

            if(!ret){
                this.setFocusToFirstItem();

            }else{

                List<Record2> records = this.tblDetail.getModelObject();

                for(int i = 0;i < details.size();i++){
                    
                    CC2060S02.Detail2 detail = details.get(i);
                    Record2 record = new Record2();
                    record.chkCommit = new ExCheckBox("chkCommit", new Model<Boolean>(false), i);
                    record.chkDelete = new ExCheckBox("chkDelete", new Model<Boolean>(false), i);
                    record.txtCompanyCode = new ExTextField<String>("txtCompanyCode", new Model<String>(detail.companyCode), i);
                    record.txtMenuId = new ExTextField<String>("txtMenuId", new Model<String>(detail.menuId), i);
                    record.txtUpdDate = new ExTextField<String>("txtUpdDate", new Model<String>(detail.updDate), i);
                    record.txtMenuKbn = new ExTextField<String>("txtMenuKbn", new Model<String>(detail.menuKbn), i);
                    record.txtMajorId = new ExTextField<String>("txtMajorId", new Model<String>(detail.majorId), i);
                    record.txtMinorId = new ExTextField<String>("txtMinorId", new Model<String>(detail.minorId), i);
                    record.txtScreenId = new ExTextField<String>("txtScreenId", new Model<String>(detail.screenId), i);
                    record.txtMenuNo = new ExTextField<String>("txtMenuNo", new Model<String>(Integer.toString(detail.menuNo)), i);
                    record.txtMenuNm = new ExTextField<String>("txtMenuNm", new Model<String>(detail.menuNm), i);
                    record.txtClassNm = new ExTextField<String>("txtClassNm", new Model<String>(detail.classNm), i);
   
                    record.revision = detail.revision;
                    record.operation = OPERATIONMODE_UPDATE;
                    records.add(record);

//                    CC2060S02.Detail detail = details.get(i);
//                    Record record = new Record();
//                    record.chkCommit = new ExCheckBox("chkCommit", new Model<Boolean>(false), i);
//                    record.chkDelete = new ExCheckBox("chkDelete", new Model<Boolean>(false), i);
//                    record.txtAnkenNo = new ExTextField<String>("txtAnkenNo", new Model<String>(detail.ankenNo), i);
//                    record.lstAnkenKbn = new ExDropDownChoice<ExDropDownItem>("lstAnkenKbn", new Model<ExDropDownItem>(new ExDropDownItem(detail.ankenKbn)), this.lstAnkenKbnChoices, i);
//                    record.lstEigyoKbn = new ExDropDownChoice<ExDropDownItem>("lstEigyoKbn", new Model<ExDropDownItem>(new ExDropDownItem(detail.eigyoKbn)), this.lstEigyoKbnChoices, i);
//                    record.lstShinchokuKbn = new ExDropDownChoice<ExDropDownItem>("lstShinchokuKbn", new Model<ExDropDownItem>(new ExDropDownItem(detail.shinchokuKbn)), this.lstShinchokuKbnChoices, i);
//                    record.txtAnkenMei = new ExTextField<String>("txtAnkenMei", new Model<String>(detail.ankenMei), i);
//                    record.txtEigyoTantoCd = new ExTextField<String>("txtEigyoTantoCd", new Model<String>(detail.eigyoTantoCd), i);
//                    record.txtEigyoTantoMei = new ExTextField<String>("txtEigyoTantoMei", new Model<String>(detail.eigyoTantoMei), i);
//
//                    // record.txtJuchubi = new ExTextField<String>("txtJuchubi" , new Model<String>( DateUtility.toDisplayFormat(detail.juchubi, DateUtility.VALUE_FORMAT_YMD)), i);
//                    String juchubi = (detail.juchubi == null) ? "" : DateUtility.toDisplayFormat(detail.juchubi, DateUtility.VALUE_FORMAT_YMD);
//                    record.txtJuchubi = new ExTextField<String>("txtJuchubi", new Model<String>(juchubi), i);
//
//                    record.txtJuchuKingaku = new ExTextField<String>("txtJuchuKingaku", new Model<String>(NumberUtility.toDisplayFormat(detail.juchuKingaku)), i);
//                    record.revision = detail.revision;
//                    record.operation = OPERATIONMODE_UPDATE;
//                    records.add(record);
//
//                    record.txtAnkenNo.setClientTextValidator(ExTextField.CHARSET_NUMERIC, ExTextField.ALLOWSPACE_NONE, ExTextField.CAST_IGNORE_CASE);
//                    record.txtEigyoTantoCd.setClientTextValidator(ExTextField.CHARSET_ALPHA_NUMERIC, ExTextField.ALLOWSPACE_NONE, ExTextField.CAST_UPPER_CASE);
//                    record.txtJuchubi.setClientDateValidator(ExTextField.DATE_DISP_FORMAT_YMD);
//                    record.txtJuchubi.add(new ExDatePicker());
//                    record.txtJuchuKingaku.setClientNumberValidator(0, 9999999);

                }
            }

        }catch(LogicalException e){

            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Search Master Maintenance execution is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }

            // スタックトレース出力(システムエラー)
            this.writeStackTrace(e);
            throw new IllegalStateException(e);

        }

        return ret;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return 検索処理における後処理の成否
     */
    @Override
    protected boolean postSearch(){

        // --------------------------------------------------
        // 条件部
        // --------------------------------------------------

//        this.setContainerEnabled(this.pnlCondition, false);

        // --------------------------------------------------
        // 明細部
        // --------------------------------------------------

        this.setContainerEnabled(this.pnlDetail, true);

        // --------------------------------------------------
        // 処理モード
        // --------------------------------------------------

        this.operationMode = OPERATIONMODE_UPDATE;

        // --------------------------------------------------
        // ファンクション部
        // --------------------------------------------------

        // [F04]選択
        this.setFunction04Enabled(true);
        // [F05]検索
//        this.setFunction05Enabled(false);
        // [F06]追加
        this.setFunction06Enabled(true);
        // [F07]削除
        this.setFunction07Enabled(true);
        // [F08]確定
        this.setFunction08Enabled(true);
//        // [F09]詳細
//        this.setFunction09Enabled(true);
//        // [F11]印刷
//        this.setFunction11Enabled(true);

        // --------------------------------------------------
        // フォーカス設定
        // --------------------------------------------------

        this.setFocusToFirstItem();

        return true;
    }

    /**
     * <p>起動条件 : F6(追加)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return 登録処理における前処理の成否
     */
    @Override
    protected boolean preAdd(){
        return true;
    }

    /**
     * <p>起動条件 : F6(追加)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return 登録本処理の成否
     */
    @Override
    protected boolean executeAdd(){

        // --------------------------------------------------
        // 追加の実行
        // --------------------------------------------------

        List<Record2> records = this.tblDetail.getModelObject();

        int i = records.size();

        Record2 record = new Record2();
        record.chkCommit = new ExCheckBox("chkCommit", new Model<Boolean>(false), i);
        record.chkDelete = new ExCheckBox("chkDelete", new Model<Boolean>(false), i);
        record.txtCompanyCode = new ExTextField<String>("txtCompanyCode", new Model<String>(), i);
        record.txtMenuId = new ExTextField<String>("txtMenuId", new Model<String>(), i);
        record.txtUpdDate = new ExTextField<String>("txtUpdDate", new Model<String>(), i);
        record.txtMenuKbn = new ExTextField<String>("txtMenuKbn", new Model<String>(), i);
        record.txtMajorId = new ExTextField<String>("txtMajorId", new Model<String>(), i);
        record.txtMinorId = new ExTextField<String>("txtMinorId", new Model<String>(), i);
        record.txtScreenId = new ExTextField<String>("txtScreenId", new Model<String>(), i);
        record.txtMenuNo = new ExTextField<String>("txtMenuNo", new Model<String>(), i);
        record.txtMenuNm = new ExTextField<String>("txtMenuNm", new Model<String>(), i);
        record.txtClassNm = new ExTextField<String>("txtClassNm", new Model<String>(), i);
        record.revision = 1;
        record.operation = OPERATIONMODE_INSERT;
        records.add(record);

//        Record record = new Record();
//        record.chkCommit = new ExCheckBox("chkCommit", new Model<Boolean>(false), i);
//        record.chkDelete = new ExCheckBox("chkDelete", new Model<Boolean>(false), i);
//        record.txtAnkenNo = new ExTextField<String>("txtAnkenNo", new Model<String>(), i);
//        record.lstAnkenKbn = new ExDropDownChoice<ExDropDownItem>("lstAnkenKbn", new Model<ExDropDownItem>(new ExDropDownItem("")), this.lstAnkenKbnChoices, i);
//        record.lstEigyoKbn = new ExDropDownChoice<ExDropDownItem>("lstEigyoKbn", new Model<ExDropDownItem>(new ExDropDownItem("")), this.lstEigyoKbnChoices, i);
//        record.lstShinchokuKbn = new ExDropDownChoice<ExDropDownItem>("lstShinchokuKbn", new Model<ExDropDownItem>(new ExDropDownItem("")), this.lstShinchokuKbnChoices, i);
//        record.txtAnkenMei = new ExTextField<String>("txtAnkenMei", new Model<String>(), i);
//        record.txtEigyoTantoCd = new ExTextField<String>("txtEigyoTantoCd", new Model<String>(), i);
//        record.txtEigyoTantoMei = new ExTextField<String>("txtEigyoTantoMei", new Model<String>(), i);
//        record.txtJuchubi = new ExTextField<String>("txtJuchubi", new Model<String>(), i);
//        record.txtJuchuKingaku = new ExTextField<String>("txtJuchuKingaku", new Model<String>(), i);
//        record.revision = 1;
//        record.operation = OPERATIONMODE_INSERT;
//        records.add(record);
//
//        record.txtAnkenNo.setClientTextValidator(ExTextField.CHARSET_NUMERIC, ExTextField.ALLOWSPACE_NONE, ExTextField.CAST_IGNORE_CASE);
//        record.txtEigyoTantoCd.setClientTextValidator(ExTextField.CHARSET_ALPHA_NUMERIC, ExTextField.ALLOWSPACE_NONE, ExTextField.CAST_UPPER_CASE);
//        record.txtJuchubi.setClientDateValidator(ExTextField.DATE_DISP_FORMAT_YMD);
//        record.txtJuchubi.add(new ExDatePicker());
//        record.txtJuchuKingaku.setClientNumberValidator(0, 9999999);

        return true;
    }

    /**
     * <p>起動条件 : F6(追加)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return 登録処理における後処理の成否
     */
    @Override
    protected boolean postAdd(){

        // --------------------------------------------------
        // 条件部
        // --------------------------------------------------

        this.setContainerEnabled(this.pnlCondition, false);

        // --------------------------------------------------
        // 明細部
        // --------------------------------------------------

        this.setContainerEnabled(this.pnlDetail, true);

        // --------------------------------------------------
        // 処理モード
        // --------------------------------------------------

        if(this.operationMode == OPERATIONMODE_INIT){
            this.operationMode = OPERATIONMODE_INSERT;
        }

        // --------------------------------------------------
        // ファンクション部
        // --------------------------------------------------

        // [F04]選択
        this.setFunction04Enabled(true);
        // [F05]検索
//        this.setFunction05Enabled(false);
        // [F06]追加
        this.setFunction06Enabled(true);
        // [F07]削除
        if(this.operationMode == OPERATIONMODE_INSERT){
            this.setFunction07Enabled(false);
        }else{
            this.setFunction07Enabled(true);
        }
        // [F08]確定
        this.setFunction08Enabled(true);
//        // [F09]詳細
//        this.setFunction09Enabled(false);
//        // [F11]印刷
//        this.setFunction11Enabled(false);

        // --------------------------------------------------
        // フォーカス設定
        // --------------------------------------------------

        // 最終ページを表示
        this.tblDetail.setCurrentPage(this.tblDetail.getPageCount());
        // 最終項目にフォーカスを設定
        List<Record2> records = this.tblDetail.getModelObject();
//        this.setFocus(records.get(records.size() - 1).txtAnkenNo);
        this.setFocus(records.get(records.size() - 1).txtCompanyCode);

        return true;
    }

    /**
     * <p>起動条件 : F7(削除)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return 削除処理における前処理の成否
     */
    @Override
    protected boolean preDelete(){

        boolean ret = false;

        int count = 0;

        // --------------------------------------------------
        // 削除対象のチェック
        // --------------------------------------------------

        List<Record2> records = this.tblDetail.getModelObject();
        for(Record2 record:records){
            if(record.chkDelete.getModelObject()){
                count++;
                break;
            }
        }

        // 対象が選択されていない場合はエラー
        if(count <= 0){
            // メッセージ表示(対象の未選択)
            this.showMessage("CMW00016");
            // 先頭項目にフォーカスを設定
            this.setFocusToFirstItem();
        }else{
            ret = true;
        }

        return ret;
    }

    /**
     * <p>起動条件 : F7(削除)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return 削除本処理の成否
     */
    @Override
    protected boolean executeDelete(){

        // --------------------------------------------------
        // 明細部の削除
        // --------------------------------------------------

        List<Record2> records = this.tblDetail.getModelObject();
        for(int i = 0;i < records.size();i++){
            Record2 record = records.get(i);
            if(record.chkDelete.getModelObject()){
                // 削除対象を保存
                if(record.operation == OPERATIONMODE_UPDATE){
                    if(this.tblRemoved == null){
                        this.tblRemoved = new ArrayList<Record2>();
                    }
                    record.operation = OPERATIONMODE_DELETE;
                    this.tblRemoved.add(record);
                }

                // 選択行を削除
                records.remove(i--);
            }
        }

        return true;
    }

    /**
     * <p>起動条件 : F7(削除)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return 削除処理における後処理の成否
     */
    @Override
    public boolean postDelete(){

        // --------------------------------------------------
        // 条件部
        // --------------------------------------------------

        // --------------------------------------------------
        // 明細部
        // --------------------------------------------------

        // --------------------------------------------------
        // ファンクション部
        // --------------------------------------------------

        // --------------------------------------------------
        // 処理モード
        // --------------------------------------------------

        this.operationMode = OPERATIONMODE_DELETE;

        // --------------------------------------------------
        // フォーカス設定
        // --------------------------------------------------

        this.setFocusToFirstItem();

        // 削除メッセージを表示
        this.showMessage("CMW00005");

        return true;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 前処理を実行する</p>
     * 
     * @return 確定処理における前処理の成否
     */
    @Override
    protected boolean preCommit(){

        boolean ret = false;
        boolean hasErrors = false;

        int count = 0;

        // --------------------------------------------------
        // エラーのクリア
        // --------------------------------------------------

        this.clearContainerError(this.pnlDetail);

        // --------------------------------------------------
        // 明細部の入力チェック
        // --------------------------------------------------

        List<Record2> records = this.tblDetail.getModelObject();
        for(Record2 record:records){
            if(record.chkCommit.getModelObject()){
                count++;

                // 会社コード
                if(!this.checkTextRequired(hasErrors, record.txtCompanyCode, "会社コード", 6, 6)){
                    hasErrors = true;
                }else if(!this.checkTextCharactor(hasErrors, record.txtCompanyCode, "会社コード", PrognerRegExpJavaPattern.CHARSET_NUMERIC)){
                    hasErrors = true;
                }

                // メニューID
                if(!this.checkTextRequired(hasErrors, record.txtMenuId, "メニューID", 2, 6)){
                    hasErrors = true;
                }else if(!this.checkTextCharactor(hasErrors, record.txtMenuId, "メニューID", PrognerRegExpJavaPattern.CHARSET_ALPHA_NUMERIC)){
                    hasErrors = true;
                }

                // メニュー区分
                if(!this.checkTextRequired(hasErrors, record.txtMenuKbn, "メニュー区分", 1, 1)){
                    hasErrors = true;
                }else if(!this.checkTextCharactor(hasErrors, record.txtMenuKbn, "メニュー区分", PrognerRegExpJavaPattern.CHARSET_ALPHA_NUMERIC)){
                    hasErrors = true;
                }

                // メニュー項番
                if(!this.checkTextRequired(hasErrors, record.txtMenuNo, "メニュー項番", 1, 2)){
                    hasErrors = true;
                }else if(!this.checkTextCharactor(hasErrors, record.txtMenuNo, "メニュー項番", PrognerRegExpJavaPattern.CHARSET_NUMERIC)){
                    hasErrors = true;
                }

                // メニュー名
                if(!this.checkTextRequired(hasErrors, record.txtMenuNm, "メニュー名", 1, 40)){
                    hasErrors = true;
                }else if(!this.checkTextCharactor(hasErrors, record.txtMenuNm, "メニュー名", PrognerRegExpJavaPattern.CHARSET_ALL)){
                    hasErrors = true;
                }

//                // 案件NO
//                if(!this.checkTextRequired(hasErrors, record.txtAnkenNo, "案件NO", 6, 6)){
//                    hasErrors = true;
//                }else if(!this.checkTextCharactor(hasErrors, record.txtAnkenNo, "案件NO", PrognerRegExpJavaPattern.CHARSET_NUMERIC)){
//                    hasErrors = true;
//                }
//
//                // 案件区分
//                if(!this.checkTextRequired(hasErrors, record.lstAnkenKbn, "案件区分")){
//                    hasErrors = true;
//                }
//
//                // 営業区分
//                if(!this.checkTextRequired(hasErrors, record.lstEigyoKbn, "営業区分")){
//                    hasErrors = true;
//                }
//
//                // 進捗状況
//                if(!this.checkTextRequired(hasErrors, record.lstShinchokuKbn, "進捗状況")){
//                    hasErrors = true;
//                }
//
//                // 案件名
//                if(!this.checkTextRequired(hasErrors, record.txtAnkenMei, "案件名", 1, 30)){
//                    hasErrors = true;
//                }else if(!this.checkTextCodePoint(hasErrors, record.txtAnkenMei, "案件名")){
//                    hasErrors = true;
//                }
//
//                // 営業担当
//                // null値でなく空文字でなければ検証を行う
//                if(!((record.txtEigyoTantoCd.getValue() == null) || ("".equals(record.txtEigyoTantoCd.getValue())))){
//                    if(!this.checkTextLength(hasErrors, record.txtEigyoTantoCd, "営業担当", 5, 10)){
//                        hasErrors = true;
//                    }else if(!this.checkTextCharactor(hasErrors, record.txtEigyoTantoCd, "営業担当", PrognerRegExpJavaPattern.CHARSET_ALPHA_NUMERIC)){
//                        hasErrors = true;
//                    }
//                }
//
//                // 受注日
//                // null値でなく空文字でなければ検証を行う
//                if(!((record.txtJuchubi.getValue() == null) || ("".equals(record.txtJuchubi.getValue())))){
//                    if(!this.checkTextDate(hasErrors, record.txtJuchubi, "受注日", DateUtility.DISP_FORMAT_YMD)){
//                        hasErrors = true;
//                    }
//                }
//
//                // 受注金額
//                // null値でなく空文字でなければ検証を行う
//                if(!((record.txtJuchuKingaku.getValue() == null) || ("".equals(record.txtJuchuKingaku.getValue())))){
//                    if(!this.checkTextNumber(hasErrors, record.txtJuchuKingaku, "受注金額", 0, 9999999)){
//                        hasErrors = true;
//                    }
//                }
            }
        }

        // 対象が選択されていない場合はエラー
        if(count <= 0 && this.tblRemoved == null){
            hasErrors = true;
            // メッセージ表示(対象の未選択)
            this.showMessage("CMW00016");
        }

        // --------------------------------------------------
        // エラーの判定
        // --------------------------------------------------

        if(!hasErrors){
            ret = true;
        }

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return 確定本処理の成否
     */
    @Override
    protected boolean executeCommit(){

        boolean ret = false;

        // --------------------------------------------------
        // 確定の実行
        // --------------------------------------------------

        try{
            // サービスの取得
            CC2060S02 service = this.getService();

            // 引数(IN)の設定(登録・更新データ)
            List<CC2060S02.Detail2> details = new ArrayList<CC2060S02.Detail2>();
            List<Record2> records = this.tblDetail.getModelObject();

            for(Record2 record:records){

                if(!record.chkCommit.getModelObject()){
                    continue;
                }

                CC2060S02.Detail2 detail = service.new Detail2();

                detail.companyCode = record.txtCompanyCode.getModelObject();
                detail.menuId = record.txtMenuId.getModelObject();
                detail.updDate = record.txtUpdDate.getModelObject();
                detail.menuKbn = record.txtMenuKbn.getModelObject();
                detail.majorId = record.txtMajorId.getModelObject();
                detail.minorId = record.txtMinorId.getModelObject();
                detail.screenId = record.txtScreenId.getModelObject();
                detail.menuNo = Integer.parseInt(record.txtMenuNo.getModelObject());
                detail.menuNm = record.txtMenuNm.getModelObject();
                detail.classNm = record.txtClassNm.getModelObject();  
                
                detail.revision = record.revision;
                detail.operation = record.operation;
                details.add(detail);

//                CC2060S02.Detail detail = service.new Detail();
//
//                detail.ankenNo = record.txtAnkenNo.getModelObject();
//                detail.ankenKbn = record.lstAnkenKbn.getModelObject().getId();
//                detail.eigyoKbn = record.lstEigyoKbn.getModelObject().getId();
//                detail.shinchokuKbn = record.lstShinchokuKbn.getModelObject().getId();
//                detail.ankenMei = record.txtAnkenMei.getModelObject();
//                detail.eigyoTantoCd = record.txtEigyoTantoCd.getModelObject();
//
//                // 入力値がnullでなく空文字でない場合
//                if(record.txtJuchubi.getModelObject() != null && !"".equals(record.txtJuchubi.getModelObject())){
//                    detail.juchubi = DateUtility.toValueFormat(record.txtJuchubi.getModelObject(), DateUtility.DISP_FORMAT_YMD);
//                }
//
//                // 入力値がnullでなく空文字でない場合
//                if(record.txtJuchuKingaku.getModelObject() != null && !"".equals(record.txtJuchuKingaku.getModelObject())){
//                    detail.juchuKingaku = Double.parseDouble(NumberUtility.toValueFormatAsString(record.txtJuchuKingaku.getModelObject()));
//                }
//
//                detail.revision = record.revision;
//                detail.operation = record.operation;
//                details.add(detail);

            }

            // 引数(IN)の設定(削除データ)
            List<CC2060S02.Detail2> removed = new ArrayList<CC2060S02.Detail2>();

            if(this.tblRemoved != null){

                removed = new ArrayList<CC2060S02.Detail2>();

                for(Record2 record:this.tblRemoved){
                    CC2060S02.Detail2 detail = service.new Detail2();
                    detail.companyCode = record.txtCompanyCode.getModelObject();
                    detail.menuId = record.txtMenuId.getModelObject();
//                    detail.ankenNo = record.txtAnkenNo.getModelObject();
                    detail.revision = record.revision;
                    detail.operation = record.operation;
                    removed.add(detail);
                }

            }

            // サービスの実行
            ret = service.executeCommit(details, removed);

            // 実行結果の表示
            this.showMessage(service.getMessageModel());

            if(!ret){

                this.setFocusToFirstItem();
                int i = 0;
                boolean hasErrors = false;

                for(Record2 record:records){
                    if(!record.chkCommit.getModelObject()){
                        continue;
                    }
                    CC2060S02.Detail2 detail = details.get(i++);

//                    // 案件NO
//                    if(!detail.getFieldError(detail.ANKEN_NO).equals("")){
//                        this.setFieldError(record.txtAnkenNo);
//                        this.setFocus(hasErrors, record.txtAnkenNo);
//                        hasErrors = true;
//                    }
//
//                    // 営業担当
//                    if(!detail.getFieldError(detail.EIGYO_TANTO_CD).equals("")){
//                        this.setFieldError(record.txtEigyoTantoCd);
//                        this.setFocus(hasErrors, record.txtEigyoTantoCd);
//                        hasErrors = true;
//                    }
                }
            }

            // 監査ログ出力
            AppSession session = (AppSession)this.getSession();
            LoginModel loginModel = session.getLoginModel();
            AuditLogger.write(
                    loginModel.getCompanyCode(),
                    loginModel.getUserId(),
                    getRemoteAddress(),
                    getURI(),
                    AuditLogger.ACCESS_RESULT_SUCCESS,
                    loginModel.getLoginLevel(),
                    getProgramId(),
                    getOperationId(),
                    service.getMessageModel().getMessage()
                    );

        }catch(LogicalException e){
            
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Master Maintenance search service execution is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }
            
            // スタックトレース出力(システムエラー)
            this.writeStackTrace(e);
            throw new IllegalStateException(e);
            
        }

        return ret;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 後処理を実行する</p>
     * 
     * @return 確定処理における後処理の成否
     */
    @Override
    protected boolean postCommit(){

        boolean ret = false;

        // --------------------------------------------------
        // 画面を再描画
        // --------------------------------------------------

        this.executeClear();

        if(this.operationMode == OPERATIONMODE_INSERT){

            ret = this.executeAdd();
            if(ret){
                ret = this.postAdd();
            }

        }else{

            ret = this.executeSearch();
            if(ret){
                ret = this.postSearch();
            }

        }

        return ret;
    }

    protected void lstItemNameChange(Component sender, AjaxRequestTarget target){

//        boolean ret = false;

        // 基底クラスにAJAXターゲットを通知
        this.ajaxTarget = target;

        // メッセージをクリア
        this.clearMessage();
        
        String val = this.lstItemName.getModelObject().getName();
        List<ExDropDownItem> choices;
        
        if (val.endsWith(APPENDIX_TYPE_STRING)) {
            choices = this.lstStringConditionChoices;
        }
        else if(val.endsWith(APPENDIX_TYPE_NUMBER)) {
            choices = this.lstNumberConditionChoices;
        }
        else {
            choices = new ArrayList<ExDropDownItem>();
        }
        
        if (this.lstCondition.getChoices() != choices) {
            this.lstCondition.setChoices(choices);
            this.lstCondition.setModelValue(new String[]{"",""});
            this.txtValue.setModelObject("");
        }
        else {
            return;
        }

        // 変更対象をAJAX描画対象に追加
        target.addComponent(this.txtValue);
        target.addComponent(this.lstCondition);

//        // 小分類リストをクリア
//        this.lstMinorId.setChoices(new ArrayList<ExDropDownItem>());
//        // 画面名テーブルをクリア
//        this.tblScreenItem.setModelObject(new ArrayList<Record>());

        // --------------------------------------------------
        // 
        // --------------------------------------------------

    }    
}
