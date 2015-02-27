/*------------------------------------------------------------------------------
 * PROGNER EX for Java Servlet
 * Copyright(C) 2008 - 2014 Canon IT Solutions Inc. All rights reserved.
 *----------------------------------------------------------------------------*/
package jp.co.canonits.prognerex.aptemplate_desktopaplike.CX9010.service;

import java.util.List;

import jp.co.canonits.prognerex.aptemplate_desktopaplike.dto.BaseServiceParameters;
import jp.co.canonits.prognerex.aptemplate_desktopaplike.service.BaseService;

/**
 * <p>サービステンプレート</p>
 * 
 * @author Canon IT Solutions Inc. R&amp;D Center
 * @version 2.3
 */
public class CX9010S01 extends BaseService{

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;

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

    // --------------------------------------------------------------------------
    // 公開クラス
    // --------------------------------------------------------------------------

    // ------------------------------------------------------
    // I/Oパラメータ
    // ------------------------------------------------------

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

    }

    // --------------------------------------------------------------------------
    // 公開メソッド
    // --------------------------------------------------------------------------

    /**
     * <p>起動条件 : コンストラクタ</p>
     * <p>処理概要 : サービスを生成</p>
     */
    public CX9010S01(){

        // --------------------------------------------------
        // プログラム情報
        // --------------------------------------------------

        // 処理ID
        this.setOperationId("CX9010");
        // プログラムID
        this.setProgramId("CX9010S01");
        // プログラム名
        this.setProgramName("サービステンプレート");
        // プログラムリビジョン
        this.setProgramRevision("01.00");
    }

    /**
     * <p>起動条件 : 初期処理時</p>
     * <p>処理概要 : 初期処理を実行する</p>
     * 
     * @param choices (O)リストボックスデータ
     * @return 処理結果
     */
    public boolean executeInit(Choices choices){
        return true;
    }

    /**
     * <p>起動条件 : F5(検索)押下時</p>
     * <p>処理概要 : 検索処理を実行する</p>
     * 
     * @param condition (I)条件データ
     * @param details (O)明細データ
     * @return 検索処理の成否
     */
    public boolean executeSearch(Condition condition, List<Detail> details){
        return true;
    }

    /**
     * <p>起動条件 : F8(確定)押下時</p>
     * <p>処理概要 : 確定処理を実行する</p>
     * 
     * @param details (I)明細データ
     * @param removed (I)削除データ
     * @return 確定処理の成否
     */
    public boolean executeCommit(List<Detail> details, List<Detail> removed){
        return true;
    }

    // --------------------------------------------------------------------------
    // 内部メソッド
    // --------------------------------------------------------------------------

}
