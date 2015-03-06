/*------------------------------------------------------------------------------
 * PROGNER EX for Java Servlet
 * Copyright(C) 2008 - 2014 Canon IT Solutions Inc. All rights reserved.
 *----------------------------------------------------------------------------*/
package jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2060.page;

import java.util.ArrayList;
import java.util.List;

import jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2000.page.CodeGetter;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2060.service.CC2060S02;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.CC2060.service.CC2060S02Mockup;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.CX1020.page.CX1020C01;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.CX1030.report.CX1030R03;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.auditlog.AuditLogger;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.component.APTemplateExListView;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.component.PrintDialog;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.component.UserDialog;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.dto.LoginModel;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.dto.MessageModel;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.page.BasePage;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.session.AppSession;
import jp.co.canonits.prognerex.core.common.dto.BaseDto;
import jp.co.canonits.prognerex.core.common.exception.LogicalException;
import jp.co.canonits.prognerex.core.common.property.PropertyManager;
import jp.co.canonits.prognerex.core.common.utility.DateUtility;
import jp.co.canonits.prognerex.core.common.utility.NumberUtility;
import jp.co.canonits.prognerex.core.common.validator.PrognerRegExpJavaPattern;
import jp.co.canonits.prognerex.core.presentation_wicket.behavior.ExAjaxBehavior;
import jp.co.canonits.prognerex.core.presentation_wicket.component.ExAjaxButton;
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
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>案件台帳検索画面</p>
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

    // --------------------------------------------------------------------------
    // 内部定数
    // --------------------------------------------------------------------------

    /**
     * 明細テーブル表示件数
     */
    protected final int ROWS_PER_PAGE = 10;

    // --------------------------------------------------------------------------
    // 内部変数
    // --------------------------------------------------------------------------

    // --------------------------------------------------
    // 条件部
    // --------------------------------------------------

    /**
     * 条件部パネル
     */
    protected ExFieldSet pnlCondition;

    /**
     * 案件NO
     */
    protected ExTextField<String> txtAnkenNo;

    /**
     * 案件名
     */
    protected ExTextField<String> txtAnkenMei;

    /**
     * 進捗状況リスト
     */
    protected ExDropDownChoice<ExDropDownItem> lstShinchokuKbn;

    /**
     * 営業担当コード
     */
    protected ExTextField<String> txtEigyoTantoCd;

    /**
     * 営業担当ダイアログ
     */
    protected UserDialog dlgEigyoTanto;

    /**
     * 営業担当名
     */
    protected ExTextField<String> txtEigyoTantoMei;

    /**
     * 受注日(FROM)
     */
    protected ExTextField<String> txtJuchubiFrom;

    /**
     * 受注日(TO)
     */
    protected ExTextField<String> txtJuchubiTo;

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

    /**
     * 明細レコード
     * 
     * @author Canon IT Solutions Inc. R&amp;D Center
     * @version 2.3
     */
    protected class Record extends BaseDto{

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

        /**
         * 案件NO
         */
        protected ExTextField<String> txtAnkenNo;

        /**
         * 案件区分
         */
        protected ExDropDownChoice<ExDropDownItem> lstAnkenKbn;

        /**
         * 営業区分
         */
        protected ExDropDownChoice<ExDropDownItem> lstEigyoKbn;

        /**
         * 進捗状況
         */
        protected ExDropDownChoice<ExDropDownItem> lstShinchokuKbn;

        /**
         * 案件名
         */
        protected ExTextField<String> txtAnkenMei;

        /**
         * 営業担当コード
         */
        protected ExTextField<String> txtEigyoTantoCd;

        /**
         * 営業担当ダイアログ
         */
        protected UserDialog dlgEigyoTanto;

        /**
         * 営業担当名
         */
        protected ExTextField<String> txtEigyoTantoMei;

        /**
         * 受注日
         */
        protected ExTextField<String> txtJuchubi;

        /**
         * 受注金額
         */
        protected ExTextField<String> txtJuchuKingaku;

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

    /**
     * 案件区分
     */
    protected List<ExDropDownItem> lstAnkenKbnChoices;

    /**
     * 営業区分
     */
    protected List<ExDropDownItem> lstEigyoKbnChoices;

    /**
     * 進捗状況
     */
    protected List<ExDropDownItem> lstShinchokuKbnChoices;

    // --------------------------------------------------
    // ダイアログ
    // --------------------------------------------------

    /**
     * 印刷ダイアログ (F11用)、Elxir用
     */
    protected PrintDialog dlgPrint11;

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

        // 案件NO
        this.txtAnkenNo = new ExTextField<String>("txtAnkenNo", new Model<String>());
        this.pnlCondition.add(this.txtAnkenNo);
        this.txtAnkenNo.setClientTextValidator(ExTextField.CHARSET_NUMERIC, ExTextField.ALLOWSPACE_NONE, ExTextField.CAST_IGNORE_CASE);

        // 案件名
        this.txtAnkenMei = new ExTextField<String>("txtAnkenMei", new Model<String>());
        this.pnlCondition.add(this.txtAnkenMei);

        // 進捗状況
        this.lstShinchokuKbn = new ExDropDownChoice<ExDropDownItem>("lstShinchokuKbn", new Model<ExDropDownItem>());
        this.pnlCondition.add(this.lstShinchokuKbn);

        // 営業担当コード
        this.txtEigyoTantoCd = new ExTextField<String>("txtEigyoTantoCd", new Model<String>());
        this.pnlCondition.add(this.txtEigyoTantoCd);
        this.txtEigyoTantoCd.setClientTextValidator(ExTextField.CHARSET_ALPHA_NUMERIC, ExTextField.ALLOWSPACE_NONE, ExTextField.CAST_UPPER_CASE);
        this.txtEigyoTantoCd.add(new ExAjaxBehavior(this.getForm(), ExAjaxBehavior.EVENT_ONCHANGE){
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target){
                txtTantoCdChange(this.getComponent(), target);
            }
        });

        // 営業担当ダイアログ
        this.dlgEigyoTanto = new UserDialog("dlgEigyoTanto");
        this.pnlCondition.add(this.dlgEigyoTanto);
        this.dlgEigyoTanto.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
            private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target){
                dlgTantoCallback(dlgEigyoTanto, target);
            }
        });

        // 営業担当ボタン
        this.pnlCondition.add(new ExAjaxButton("btnEigyoTanto", this.getForm()){
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form){
                dlgEigyoTanto.show(target);
            }
        });

        // 営業担当名
        this.txtEigyoTantoMei = new ExTextField<String>("txtEigyoTantoMei", new Model<String>());
        this.pnlCondition.add(this.txtEigyoTantoMei);

        // 受注日(FROM)
        this.txtJuchubiFrom = new ExTextField<String>("txtJuchubiFrom", new Model<String>());
        this.pnlCondition.add(this.txtJuchubiFrom);
        this.txtJuchubiFrom.setClientDateValidator(ExTextField.DATE_DISP_FORMAT_YMD);
        this.txtJuchubiFrom.add(new ExDatePicker());

        // 受注日(TO)
        this.txtJuchubiTo = new ExTextField<String>("txtJuchubiTo", new Model<String>());
        this.pnlCondition.add(this.txtJuchubiTo);
        this.txtJuchubiTo.setClientDateValidator(ExTextField.DATE_DISP_FORMAT_YMD);
        this.txtJuchubiTo.add(new ExDatePicker());

        // パネルを使用不可に設定
        this.setContainerEnabled(this.pnlCondition, false);

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

        // [F09]詳細
        this.setFunction09Label("詳　細");
        // [F11]印刷
        this.setFunction11Label("印　刷");
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

        // [F11]印刷
        this.setFunction11AjaxEvent();
        // [F12]
        // this.setFunction12AjaxEvent();

        // --------------------------------------------------
        // ファンクション部のダイアログ設定
        // --------------------------------------------------

        // [F11]印刷
        this.dlgPrint11 = new PrintDialog("dlgPrint11");
        this.getForm().add(this.dlgPrint11);
        this.dlgPrint11.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
            private static final long serialVersionUID = 1L;
            
            public void onClose(AjaxRequestTarget target){
                dlgPrint11Callback(dlgPrint11, target);
            }
        });

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
        
        item.add(record.txtMenuKbn.setMoveRowOnKeyDownScript());
        item.add(record.txtMajorId.setMoveRowOnKeyDownScript());
        item.add(record.txtMinorId.setMoveRowOnKeyDownScript());
        item.add(record.txtScreenId.setMoveRowOnKeyDownScript());
        item.add(record.txtMenuNo.setMoveRowOnKeyDownScript());
        item.add(record.txtMenuNm.setMoveRowOnKeyDownScript());
        item.add(record.txtClassNm.setMoveRowOnKeyDownScript()); 
        
//        // 案件NO
//        record.txtAnkenNo.setEnabled(record.operation == OPERATIONMODE_INSERT ? true : false);
//
//        item.add(record.txtAnkenNo.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
//
//        // 案件区分
//        item.add(record.lstAnkenKbn.setChecker(record.chkCommit));
//
//        // 営業区分
//        item.add(record.lstEigyoKbn.setChecker(record.chkCommit));
//
//        // 進捗状況
//        item.add(record.lstShinchokuKbn.setChecker(record.chkCommit));
//
//        // 案件名
//        item.add(record.txtAnkenMei.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
//
//        // 営業担当コード
//        item.add(record.txtEigyoTantoCd.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
//        record.txtEigyoTantoCd.add(new ExAjaxBehavior(this.getForm(), ExAjaxBehavior.EVENT_ONCHANGE){
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            protected void onSubmit(AjaxRequestTarget target){
//                tblDetailTxtTantoCdChange(this.getComponent(), target);
//            }
//        });
//
//        // 営業担当ダイアログ
//        record.dlgEigyoTanto = new UserDialog("dlgEigyoTanto");
//        item.add(record.dlgEigyoTanto);
//        record.dlgEigyoTanto.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
//            private static final long serialVersionUID = 1L;
//
//            public void onClose(AjaxRequestTarget target){
//                tblDetailDlgTantoCallback(record.dlgEigyoTanto, target);
//            }
//        });
//
//        // 営業担当ボタン
//        item.add(new ExAjaxButton("btnEigyoTanto", this.getForm(), record.txtEigyoTantoCd.getIndex()){
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void onSubmit(AjaxRequestTarget target, Form<?> form){
//                record.dlgEigyoTanto.show(target);
//            }
//        }.setMoveRowOnKeyDownScript());
//
//        // 営業担当名
//        item.add(record.txtEigyoTantoMei.setMoveRowOnKeyDownScript());
//
//        // 受注日
//        item.add(record.txtJuchubi.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
//
//        // 受注金額
//        item.add(record.txtJuchuKingaku.setChecker(record.chkCommit).setMoveRowOnKeyDownScript());
    }

    /**
     * <p>先頭項目にフォーカスを設定する</p>
     * 
     */
    protected void setFocusToFirstItem(){

        // 条件部
        if(this.pnlCondition.isEnabled()){
            this.setFocus(this.txtAnkenNo);
            // 明細部
        }else{
            if(this.tblDetail.getModelObject().size() > 0){
                if(operationMode == OPERATIONMODE_INSERT){
                    this.setFocus(this.tblDetail.getModelObject().get(0).txtCompanyCode);
                }else{
                    this.setFocus(this.tblDetail.getModelObject().get(0).txtUpdDate);
                }
//                if(operationMode == OPERATIONMODE_INSERT){
//                    this.setFocus(this.tblDetail.getModelObject().get(0).txtAnkenNo);
//                }else{
//                    this.setFocus(this.tblDetail.getModelObject().get(0).lstAnkenKbn);
//                }
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

                // 進捗状況
                this.lstShinchokuKbnChoices = new ArrayList<ExDropDownItem>();
                this.lstShinchokuKbnChoices.add(new ExDropDownItem());
                for(String[] item:choices.shinchokuKbn){
                    this.lstShinchokuKbnChoices.add(new ExDropDownItem(item[0], item[1]));
                }

                // 案件区分
                this.lstAnkenKbnChoices = new ArrayList<ExDropDownItem>();
                this.lstAnkenKbnChoices.add(new ExDropDownItem());
                for(String[] item:choices.ankenKbn){
                    this.lstAnkenKbnChoices.add(new ExDropDownItem(item[0], item[1]));
                }

                // 営業区分
                this.lstEigyoKbnChoices = new ArrayList<ExDropDownItem>();
                this.lstEigyoKbnChoices.add(new ExDropDownItem());
                for(String[] item:choices.eigyoKbn){
                    this.lstEigyoKbnChoices.add(new ExDropDownItem(item[0], item[1]));
                }

                // --------------------------------------------------
                // 初期値の設定
                // --------------------------------------------------

                // 進捗状況
                this.lstShinchokuKbn.setChoices(this.lstShinchokuKbnChoices);
            }

        }catch(LogicalException e){

            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Anken Daicho Search service initialization is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
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

        // --------------------------------------------------
        // 条件部
        // --------------------------------------------------

        // エラー表示のクリア
        this.clearContainerError(this.pnlCondition);

        // --------------------------------------------------
        // 明細部
        // --------------------------------------------------

        // エラー表示のクリア
        this.clearContainerError(this.pnlDetail);

        // 入力内容のクリア
        this.tblDetail.setModelObject(new ArrayList<Record2>());
        this.tblRemoved = null;

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
        // [F09]詳細
        this.setFunction09Enabled(false);
        // [F11]印刷
        this.setFunction11Enabled(false);

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

        this.clearContainerError(this.pnlCondition);

        // --------------------------------------------------
        // 条件部の入力チェック
        // --------------------------------------------------

        // null値でなく空文字でなければ検証を行う
        if(this.txtAnkenNo.getValue() != null && !"".equals(this.txtAnkenNo.getValue())){
            if(!this.checkTextLength(hasErrors, this.txtAnkenNo, "案件NO", 3, 6)){
                hasErrors = true;
            }else if(!this.checkTextCharactor(hasErrors, this.txtAnkenNo, "案件NO", PrognerRegExpJavaPattern.CHARSET_NUMERIC)){
                hasErrors = true;
            }
        }

        // 受注日(FROM)
        // null値でなく空文字でなければ検証を行う
        if(this.txtJuchubiFrom.getValue() != null && !"".equals(this.txtJuchubiFrom.getValue())){
            if(!this.checkTextDate(hasErrors, this.txtJuchubiFrom, "受注日", DateUtility.DISP_FORMAT_YMD)){
                hasErrors = true;
            }
        }

        // 受注日(TO)
        if(this.txtJuchubiTo.getValue() != null && !"".equals(this.txtJuchubiTo.getValue())){
            if(!this.checkTextDate(hasErrors, this.txtJuchubiTo, "受注日", DateUtility.DISP_FORMAT_YMD)){
                hasErrors = true;
            }
        }

        // 営業担当コード
        if(this.txtEigyoTantoCd.getValue() != null && !"".equals(this.txtEigyoTantoCd.getValue())){
            if(!this.checkTextLength(hasErrors, this.txtEigyoTantoCd, "営業担当コード", 1, 10)){
                hasErrors = true;
            }
        }

        // 案件名
        if(this.txtAnkenMei.getValue() != null && !"".equals(this.txtAnkenMei.getValue())){
            if(!this.checkTextCodePoint(hasErrors, this.txtAnkenMei, "案件名")){
                hasErrors = true;
            }else if(!this.checkTextLength(hasErrors, this.txtAnkenMei, "案件名", 1, 30)){
                hasErrors = true;
            }
        }

        // --------------------------------------------------
        // 条件部の項目間チェック
        // --------------------------------------------------

        // 受注日(FROM-TO)
        if(!hasErrors){
            String juchubiFrom = this.txtJuchubiFrom.getValue();
            juchubiFrom = (juchubiFrom == null || "".equals(juchubiFrom)) ? juchubiFrom : DateUtility.toValueFormat(this.txtJuchubiFrom.getModelObject(), DateUtility.DISP_FORMAT_YMD);

            String juchubiTo = this.txtJuchubiTo.getValue();
            juchubiTo = (juchubiTo == null || "".equals(juchubiTo)) ? juchubiTo : DateUtility.toValueFormat(this.txtJuchubiTo.getModelObject(), DateUtility.DISP_FORMAT_YMD);

            if(juchubiFrom != null && juchubiTo != null && juchubiFrom.compareTo(juchubiTo) > 0){
                hasErrors = true;
                this.setFieldError(this.txtJuchubiFrom);
                this.setFieldError(this.txtJuchubiTo);
                this.showMessage("CME00015", "受注日");
                this.setFocus(this.txtJuchubiFrom);
            }
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
                boolean hasErrors = false;

                // 営業担当
//                if(!condition.getFieldError(condition.EIGYO_TANTO_CD).equals("")){
//                    this.setFieldError(this.txtEigyoTantoCd);
//                    this.setFocus(hasErrors, this.txtEigyoTantoCd);
//                    hasErrors = true;
//                }

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
                LOGGER.error("Search Anken Daicho execution is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
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

        this.setContainerEnabled(this.pnlCondition, false);

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
        this.setFunction05Enabled(false);
        // [F06]追加
        this.setFunction06Enabled(true);
        // [F07]削除
        this.setFunction07Enabled(true);
        // [F08]確定
        this.setFunction08Enabled(true);
        // [F09]詳細
        this.setFunction09Enabled(true);
        // [F11]印刷
        this.setFunction11Enabled(true);

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
        this.setFunction05Enabled(false);
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
        // [F09]詳細
        this.setFunction09Enabled(false);
        // [F11]印刷
        this.setFunction11Enabled(false);

        // --------------------------------------------------
        // フォーカス設定
        // --------------------------------------------------

        // 最終ページを表示
        this.tblDetail.setCurrentPage(this.tblDetail.getPageCount());
        // 最終項目にフォーカスを設定
        List<Record2> records = this.tblDetail.getModelObject();
//        this.setFocus(records.get(records.size() - 1).txtAnkenNo);
        this.setFocus(records.get(records.size() - 1).txtClassNm);

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
                LOGGER.error("Anken Daicho search service execution is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
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

    /**
     * <p>起動条件 : F09(詳細)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @return 詳細本処理の成否
     */
    @Override
    protected boolean executeF09(){

        boolean ret = false;

        int count = 0;
//        String ankenNo = null;

        // --------------------------------------------------
        // 対象のチェック
        // --------------------------------------------------

        List<Record2> records = this.tblDetail.getModelObject();
        for(Record2 record:records){
            if(record.chkDelete.getModelObject()){
                count++;
//                ankenNo = record.txtAnkenNo.getModelObject();
            }
        }

        // 対象が選択されていない場合はエラー
        if(count != 1){
            if(count <= 0){
                // メッセージ表示(対象の未選択)
                this.showMessage("CMW00016");
            }else{
                // メッセージ表示(対象の複数選択)
                this.showMessage("CMW00017");
            }
            // 先頭項目にフォーカスを設定
            this.setFocusToFirstItem();
        }else{
            ret = true;
        }

        // --------------------------------------------------
        // 子画面ポップアップを設定
        // --------------------------------------------------

        if(ret){
            // ページパラメータの設定
            PageParameters params = new PageParameters();
//            params.put(CX1020C01.PARAM_ANKENNO, ankenNo);

            // ポップアップスクリプトの設定
            this.setPopupScript(CX1020C01.class, params);
        }

        return ret;
    }

    /**
     * <p>起動条件 : F11(印刷)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @param target AJAXターゲット
     * @return 印刷本処理の成否
     */
    @Override
    protected boolean executeF11(AjaxRequestTarget target){

        // --------------------------------------------------
        // 印刷の実行 (印刷ダイアログを表示する場合)
        // --------------------------------------------------

        // 印刷ダイアログの使用可否を設定
        // this.dlgPrint11.setPrintEnable(false);
        // this.dlgPrint11.setPreviewEnable(false);
        // this.dlgPrint11.setPdfEnable(false);
        // this.dlgPrint11.setCsvEnable(false);
        this.dlgPrint11.setPasswordEnable(true);

        // 印刷ダイアログを表示
        this.dlgPrint11.show(target);

        return true;
    }

    /**
     * <p>起動条件 : F11(印刷)押下時</p>
     * <p>処理概要 : 本処理を実行する</p>
     * 
     * @param target AJAXターゲット
     * @return 印刷本処理の成否
     */
    @Override
    protected boolean executePrint(AjaxRequestTarget target){

        // --------------------------------------------------
        // 印刷の実行 (印刷ダイアログを表示する場合)
        // --------------------------------------------------

        // 印刷ダイアログの使用可否を設定
        // this.dlgPrint11.setPasswordEnable(true);

        return true;
    }

    /**
     * <p>起動条件 : 条件部.営業担当コード変更時</p>
     * <p>処理概要 : コード名称を取得する</p>
     * 
     * @param sender イベント発生元
     * @param target AJAXターゲット
     */
    protected void txtTantoCdChange(Component sender, AjaxRequestTarget target){

        boolean ret = false;

        // 基底クラスにAJAXターゲットを通知
        this.ajaxTarget = target;

        // メッセージをクリア
        this.clearMessage();

        // 変更対象をAJAX描画対象に追加
        target.addComponent(this.txtEigyoTantoCd);
        target.addComponent(this.txtEigyoTantoMei);

        // 担当コードの入力エラーをクリア
        this.clearFieldError(this.txtEigyoTantoCd);
        // 営業担当名をクリア
        this.txtEigyoTantoMei.setModelObject("");

        // 営業担当コードの入力チェック
        if(this.txtEigyoTantoCd.getModelObject() == null || !this.checkTextLength(this.txtEigyoTantoCd, "営業担当", 5, 10)){
            return;
        }

        // 営業担当コードを大文字に変換
        this.txtEigyoTantoCd.setModelObject(this.txtEigyoTantoCd.getModelObject().toUpperCase());

        // --------------------------------------------------
        // 名称取得処理の実行
        // --------------------------------------------------

        try{

            AppSession session = (AppSession)this.getSession();
            LoginModel loginModel = session.getLoginModel();

            // 部品の生成
            CodeGetter parts = new CodeGetter(loginModel);
            parts.setClientLocale(this.getLocale());

            // 引数の設定
            CodeGetter.UserModel model = parts.new UserModel();

            // 部品の実行
            ret = parts.getUser(loginModel.getCompanyCode(), this.txtEigyoTantoCd.getModelObject(), model);

            // 実行結果の表示
            if(!ret){
                this.setFieldError(this.txtEigyoTantoCd);
                this.showMessage(parts.getMessageModel());

            }else{
                // 営業担当名
                this.txtEigyoTantoMei.setModelObject(model.userNm);
            }

        }catch(LogicalException e){

            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Search user's eigyo tanto code is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }

            // スタックトレース出力(システムエラー)
            this.writeStackTrace(e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>起動条件 : 明細部.営業担当コード変更時</p>
     * <p>処理概要 : コード名称を取得する</p>
     * 
     * @param sender イベント発生元
     * @param target AJAXターゲット
     */
    protected void tblDetailTxtTantoCdChange(Component sender, AjaxRequestTarget target){

        boolean ret = false;

        // 基底クラスにAJAXターゲットを通知
        this.ajaxTarget = target;

        // イベント発生元の取得
        @SuppressWarnings("unchecked")
        ListItem<Record> item = (ListItem<Record>)sender.getParent();
        Record record = item.getModelObject();

        // メッセージをクリア
        this.clearMessage();

        // 変更対象をAJAX描画対象に追加
        target.addComponent(record.txtEigyoTantoCd);
        target.addComponent(record.txtEigyoTantoMei);

        // 営業担当コードの入力エラーをクリア
        this.clearFieldError(record.txtEigyoTantoCd);
        // 営業担当名をクリア
        record.txtEigyoTantoMei.setModelObject("");

        // 営業担当コードの入力チェック
        if(record.txtEigyoTantoCd.getModelObject() == null || !this.checkTextLength(record.txtEigyoTantoCd, "営業担当", 5, 10)){
            return;
        }

        // 営業担当コードを大文字に変換
        record.txtEigyoTantoCd.setModelObject(record.txtEigyoTantoCd.getModelObject().toUpperCase());

        // --------------------------------------------------
        // 名称取得処理の実行
        // --------------------------------------------------

        try{

            AppSession session = (AppSession)this.getSession();
            LoginModel loginModel = session.getLoginModel();

            // 部品の生成
            CodeGetter parts = new CodeGetter(loginModel);
            parts.setClientLocale(this.getLocale());

            // 引数の設定
            CodeGetter.UserModel model = parts.new UserModel();

            // 部品の実行
            ret = parts.getUser(loginModel.getCompanyCode(), record.txtEigyoTantoCd.getModelObject(), model);

            // 実行結果の表示
            if(!ret){

                this.setFieldError(record.txtEigyoTantoCd);
                this.showMessage(parts.getMessageModel());

            }else{
                // 営業担当名
                record.txtEigyoTantoMei.setModelObject(model.userNm);
            }

        }catch(LogicalException e){
            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Search user's eigyo tanto name is failed. Some runtime exception happen. Please check stacktrace. " + e.getMessage());
            }
            // スタックトレース出力(システムエラー)
            this.writeStackTrace(e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>起動条件 : 営業担当ダイアログからのコールバック時</p>
     * <p>処理概要 : コード名称を表示する</p>
     * 
     * @param sender イベント発生元
     * @param target AJAXターゲット
     */
    protected void dlgTantoCallback(UserDialog sender, AjaxRequestTarget target){
        
        // ダイアログの戻り値が異常の場合は処理しない
        if(!sender.getModal().getResult()){
            return;
        }

        // 基底クラスにAJAXターゲットを通知
        this.ajaxTarget = target;

        // メッセージをクリア
        this.clearMessage();

        // 変更対象をAJAX描画対象に追加
        target.addComponent(this.txtEigyoTantoCd);
        target.addComponent(this.txtEigyoTantoMei);

        // 営業担当コードの入力エラーをクリア
        this.clearFieldError(this.txtEigyoTantoCd);

        // ダイアログからの戻り値を表示
        this.txtEigyoTantoCd.setModelObject(sender.getModal().getSelectedUserId());
        this.txtEigyoTantoMei.setModelObject(sender.getModal().getSelectedUserNm());
    }

    /**
     * <p>起動条件 : 営業担当ダイアログからのコールバック時</p>
     * <p>処理概要 : コード名称を表示する</p>
     * 
     * @param sender イベント発生元
     * @param target AJAXターゲット
     */
    protected void tblDetailDlgTantoCallback(UserDialog sender, AjaxRequestTarget target){
        
        // ダイアログの戻り値が異常の場合は処理しない
        if(!sender.getModal().getResult()){
            return;
        }

        // 基底クラスにAJAXターゲットを通知
        this.ajaxTarget = target;

        // イベント発生元の取得
        @SuppressWarnings("unchecked")
        ListItem<Record> item = (ListItem<Record>)sender.getParent();
        Record record = item.getModelObject();

        // メッセージをクリア
        this.clearMessage();

        // 変更対象をAJAX描画対象に追加
        target.addComponent(record.txtEigyoTantoCd);
        target.addComponent(record.txtEigyoTantoMei);
        target.addComponent(record.chkCommit);

        // 営業担当コードの入力エラーをクリア
        this.clearFieldError(record.txtEigyoTantoCd);

        // ダイアログからの戻り値を表示
        record.txtEigyoTantoCd.setModelObject(sender.getModal().getSelectedUserId());
        record.txtEigyoTantoMei.setModelObject(sender.getModal().getSelectedUserNm());

        // 確定チェックボックスをONに設定
        record.chkCommit.setModelObject(true);
    }

    /**
     * <p>起動条件 : 印刷ダイアログからのコールバック時</p>
     * <p>処理概要 : 帳票を印刷する</p>
     * 
     * @param sender イベント発生元
     * @param target AJAXターゲット
     */
    protected void dlgPrint11Callback(PrintDialog sender, AjaxRequestTarget target){

        // ダイアログの戻り値が異常の場合は処理しない
        if(!sender.getModal().getResult()){
            return;
        }

        // 基底クラスにAJAXターゲットを通知
        this.ajaxTarget = target;

        // メッセージをクリア
        this.clearMessage();

        // ダイアログからの戻り値を取得
        int printMode = sender.getModal().getSelectedPrintMode();
        int printCopies = sender.getModal().getPrintCopies();
        String printPassword = sender.getModal().getPrintPassword();

        // --------------------------------------------------
        // 印刷の実行 (印刷ダイアログの表示後)
        // --------------------------------------------------
        CX1030R03 report = null;

        try{

            AppSession session = (AppSession)this.getSession();
            LoginModel loginModel = session.getLoginModel();

            report = new CX1030R03(
                    null,
                    null,
                    null,
                    printMode,
                    null,
                    printCopies,
                    printPassword,
                    null,
                    null,
                    PropertyManager.getProperty("REPORT_CSV_SEPARATOR"),
                    PropertyManager.getProperty("REPORT_CSV_CHARASET"),
                    PropertyManager.getProperty("REPORT_CSV_LINE_FEED_CODE"),
                    null
                    );

            // 引数の設定
            report.setLoginModel(loginModel);
            report.setClientLocale(getLocale());
            report.setMessageModel(new MessageModel());

            report.setAnkenNo(this.txtAnkenNo.getModelObject());
            report.setShinchokuKbn(this.lstShinchokuKbn.getModelObject().getId());
            report.setEigyoTantoCd(this.txtEigyoTantoCd.getModelObject());

            if(this.txtJuchubiFrom.getModelObject() != null){
                report.setJuchubiFrom(DateUtility.toValueFormat(this.txtJuchubiFrom.getModelObject(), DateUtility.DISP_FORMAT_YMD));
            }

            if(this.txtJuchubiTo.getModelObject() != null){
                report.setJuchubiTo(DateUtility.toValueFormat(this.txtJuchubiTo.getModelObject(), DateUtility.DISP_FORMAT_YMD));
            }

            // 帳票の実行
            boolean ret = report.print();

            // 実行結果の表示
            this.showMessage(report.getMessageModel());

            // 帳票印刷のスクリプトを設定
            if(ret){
                switch(printMode){
                    // 直接印刷
                    case PRINTMODE_DIRECT:
                        this.setPrintScript(report.getPrintScript(report.getPrinterName(), printCopies));
                        break;
                    // 印刷プレビュー
                    case PRINTMODE_PREVIEW:
                        // this.setPrintScript(report.getPrintScript());
                        this.setDownloadResponce(report.getStoreFilePath(), report.getStoreFileName(), report.getSaveFileName());
                        break;
                    // 電子帳票(PDF)
                    case PRINTMODE_PDF:
                        // (表示する場合)
                        // this.setPrintScript(report.getPrintScript());
                        // (保存する場合)
                        this.setDownloadResponce(report.getStoreFilePath(), report.getStoreFileName(), report.getSaveFileName());
                        break;
                    // 電子データ(CSV)
                    case PRINTMODE_CSV:
                        // (表示する場合)
                        // this.setPrintScript(report.getPrintScript());
                        // (保存する場合)
                        this.setDownloadResponce(report.getStoreFilePath(), report.getStoreFileName(), report.getSaveFileName());
                        break;
                }
            }

        }catch(LogicalException e){

            // 帳票機能より例外がスローされてきた場合、画面メッセージの表示と
            // ログ出力のみ行い再スローしないこととする。
            // 実行結果の表示
            this.showMessage("CME00000");

            // エラーログ出力(システムエラー)
            if(LOGGER.isErrorEnabled()){
                LOGGER.error("Print Anken Daicho is failed. Some runtime exception happen on report server. Please check stacktrace. " + e.getMessage());
            }

            // スタックトレース出力(システムエラー)
            this.writeStackTrace(e);
        }

    }

}
